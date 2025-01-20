package F64.Upscale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

@Controller
public class ImageController {

    // Python 실행 파일 (가상환경)
    private static final String PYTHON_EXE = "C:\\Users\\USER\\Desktop\\F-64\\f64_image_upscaler\\venv\\Scripts\\python.exe";
    // Python 스크립트 절대경로
    private static final String PYTHON_SCRIPT_PATH = "C:\\Users\\USER\\Desktop\\F-64\\f64_image_upscaler\\inference_realesrgan.py";

    // 업로드 디렉토리 (application.properties)
    @Value("${upload.path}")
    private String uploadDir; // C:/upload

    @GetMapping("/upload")
    public String showUploadForm() {
        return "upload";
    }

    @PostMapping("/upscale")
    public String upscaleImage(@RequestParam("image") MultipartFile image,
                               @RequestParam(value = "face_enhance", required = false) String faceEnhance,
                               Model model) {
        try {
            // 1) 폴더 준비
            createDirectoryIfNotExist(uploadDir);
            String resultDir = uploadDir + File.separator + "result";
            createDirectoryIfNotExist(resultDir);

            // 2) 파일 저장 (파일명에 한글 포함 여부 체크)
            File imageFile = saveMultipartFile(image, uploadDir);

            // 3) 파이썬 스크립트 실행
            boolean useFaceEnhance = ("on".equals(faceEnhance));
            String outputLog = runPythonUpscale(
                    imageFile.getAbsolutePath(),
                    resultDir,
                    "png",
                    useFaceEnhance
            );

            // 4) 파이썬 로그에서 결과 파일 경로 찾기
            String savedPath = parseSavedPath(outputLog);
            if (savedPath == null) {
                model.addAttribute("error", "결과 파일 경로(Saved: ...)를 찾지 못했습니다.");
                return "result";
            }

            // 5) URL 경로 (HTTP) 구성
            // 원본 파일명
            String originalFilename = imageFile.getName(); // ex) "myPhoto.png"
            // => /upload/myPhoto.png
            String originalUrl = "/upload/" + originalFilename;

            // 결과 파일명
            String resultFilename = Paths.get(savedPath).getFileName().toString(); // e.g. "myPhoto_upscaled.png"
            // => /upload/result/myPhoto_upscaled.png
            String resultUrl = "/upload/result/" + resultFilename;

            // 6) 뷰에 전달
            model.addAttribute("original_image_path", originalUrl);
            model.addAttribute("result_image_path", resultUrl);

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "result";
    }

    /**
     * 파이썬 스크립트 실행
     */
    private String runPythonUpscale(String inputPath, String outputPath, String ext, boolean faceEnhance)
            throws IOException, InterruptedException {

        ProcessBuilder pb = new ProcessBuilder(
                PYTHON_EXE,
                PYTHON_SCRIPT_PATH,
                "-i", inputPath,
                "-o", outputPath,
                "--ext", ext,
                "--gpu-id", "0"
        );

        // 작업 디렉토리 지정
        pb.directory(new File("C:\\Users\\USER\\Desktop\\F-64\\f64_image_upscaler"));

        if (faceEnhance) {
            pb.command().add("--face_enhance");
        }

        Process process = pb.start();

        String outputLog = readStream(process.getInputStream());
        String errorLog  = readStream(process.getErrorStream());

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Python script error:\n" + errorLog);
        }
        return outputLog;
    }

    /**
     * upscaled.png 찾기
     */
    private String parseSavedPath(String outputLog) {
        for (String line : outputLog.split("\n")) {
            if (line.trim().startsWith("Saved:")) {
                return line.trim().substring("Saved:".length()).trim();
            }
        }
        return null;
    }

    /**
     * Stream -> String
     */
    private String readStream(InputStream is) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }

    /**
     * 폴더 생성
     */
    private void createDirectoryIfNotExist(String dirPath) throws IOException {
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    /**
     * MultipartFile -> File (한글/공백 파일명 방지)
     */
    private File saveMultipartFile(MultipartFile multipartFile, String dir) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("파일명이 비어있습니다.");
        }

        // 한글/공백/특수문자 검사 (정규식 예시)
        // 아래는 "영문자, 숫자, _, ., -" 만 허용
        // 필요하다면 더 세부 조정 가능
        if (!Pattern.matches("^[a-zA-Z0-9_.\\-]+$", originalFilename)) {
            throw new IllegalArgumentException("파일명에 한글 또는 특수문자가 포함되어 있습니다: " + originalFilename);
        }

        // 파일 저장
        File targetDir = new File(dir);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        File targetFile = new File(targetDir, originalFilename);
        multipartFile.transferTo(targetFile);

        return targetFile;
    }

}
