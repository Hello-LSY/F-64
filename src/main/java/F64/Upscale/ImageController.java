package F64.Upscale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

@Controller
public class ImageController {

    // 1) application.properties에서 주입받는 값들
    @Value("${upload.path}")
    private String uploadDir;

    @Value("${f64.python.exe}")
    private String pythonExePath;

    @Value("${f64.python.script}")
    private String pythonScriptPath;

    @Value("${f64.python.workingdir}")
    private String pythonWorkingDir;

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

            // 2) 파일 저장
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
            String originalFilename = imageFile.getName();
            String originalUrl = "/upload/" + originalFilename;

            String resultFilename = Paths.get(savedPath).getFileName().toString();
            String resultUrl = "/upload/result/" + resultFilename;

            // 6) 뷰에 데이터 전달
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
    private String runPythonUpscale(String inputPath,
                                    String outputPath,
                                    String ext,
                                    boolean faceEnhance)
            throws IOException, InterruptedException {

        ProcessBuilder pb = new ProcessBuilder(
                pythonExePath,      // application.properties에서 주입받은 경로
                pythonScriptPath,   // application.properties에서 주입받은 경로
                "-i", inputPath,
                "-o", outputPath,
                "--ext", ext,
                "--gpu-id", "0"
        );

        // **파이썬 작업 디렉토리**도 application.properties로 옮김
        pb.directory(new File(pythonWorkingDir));

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

    private String parseSavedPath(String outputLog) {
        for (String line : outputLog.split("\n")) {
            if (line.trim().startsWith("Saved:")) {
                return line.trim().substring("Saved:".length()).trim();
            }
        }
        return null;
    }

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

    private void createDirectoryIfNotExist(String dirPath) throws IOException {
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    private File saveMultipartFile(MultipartFile multipartFile, String dir) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("파일명이 비어있습니다.");
        }

        // "영문, 숫자, _, ., -" 외 문자 금지
        if (!Pattern.matches("^[a-zA-Z0-9_.\\-]+$", originalFilename)) {
            throw new IllegalArgumentException("파일명에 한글/특수문자가 포함되어 있습니다: " + originalFilename);
        }

        File targetDir = new File(dir);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        File targetFile = new File(targetDir, originalFilename);
        multipartFile.transferTo(targetFile);

        return targetFile;
    }

}
