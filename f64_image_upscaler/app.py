import subprocess
import os
import cv2
import time

from flask import Flask, render_template, request, jsonify
from werkzeug.utils import secure_filename
from skimage.metrics import structural_similarity as ssim
from skimage.metrics import peak_signal_noise_ratio as psnr

app = Flask(__name__)
UPLOAD_FOLDER = 'C:\\Users\\USER\\Desktop\\F-64\\src\\main\\resources\\static\\results'
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/upscale', methods=['POST'])
def upscale_image():
    try:
        # 받은 이미지 저장 (POST 요청으로 전달된 이미지 데이터를 가정하고 있습니다)
        input_image_filename = secure_filename(request.files['image'].filename)
        input_image_path = os.path.join(app.config['UPLOAD_FOLDER'], input_image_filename)
        input_image_name, input_image_extension = os.path.splitext(input_image_filename)

        # 결과 이미지의 이름 생성
        output_image_filename = os.path.splitext(input_image_filename)[0] + '_upscale'
        output_image_filename_out = input_image_name + '_out' + input_image_extension
        output_image_path = os.path.join(app.config['UPLOAD_FOLDER'], output_image_filename)

        request.files['image'].save(input_image_path)

        # RealESRGAN 실행 명령어
        command = ['python', 'inference_realesrgan.py', '-n', 'RealESRGAN_x4plus.pth', '-i', input_image_path, '-o', output_image_path]
        start_time = time.time()
        face_enhance = request.form.get('face_enhance')
        if face_enhance and face_enhance == 'on':
            command.append('--face_enhance')

        # 명령어 실행
        result = subprocess.run(command, capture_output=True, text=True)

        end_time = time.time()  # 처리 시간 측정 종료
        processing_time = end_time - start_time  # 처리 시간 계산

        original_image = cv2.imread(input_image_path)
        processed_image = cv2.imread(output_image_path + "\\" + output_image_filename_out)
        processed_image_size = os.path.getsize(output_image_path + "\\" + output_image_filename_out)
        processed_image_resolution = f"{processed_image.shape[1]}x{processed_image.shape[0]}"
        processed_image_resize = cv2.resize(processed_image, (original_image.shape[1], original_image.shape[0]))
        original_image_size = os.path.getsize(input_image_path)
        original_image_resolution = f"{original_image.shape[1]}x{original_image.shape[0]}"

        # # SSIM 및 PSNR 계산
        ssim_value, _ = ssim(original_image, processed_image_resize, channel_axis=-1, full=True)
        psnr_value = psnr(original_image, processed_image_resize)

        # SSIM 및 PSNR 값 출력
        print()
        print(f'Image Size: {convert_bytes(original_image_size)} -> {convert_bytes(processed_image_size)}')
        print(f'Image Resolution: {original_image_resolution} -> {processed_image_resolution}')
        print("SSIM:", ssim_value)
        print("PSNR:", psnr_value)
        print("Processing Time:", processing_time)
        print()
        # print("command:", command)
        # print("Request Form Data:", request.form)
        # print("Request Files Data:", request.files)
        # print("Request Data:", request.data)
        # print("Request Args:", request.args)
        # print("input_image_path :" ,input_image_path)
        # print("input_image_filename :",input_image_filename)
        # print("output_image_path :" ,output_image_path)
        # print("output_image_filename :" , output_image_filename_out)

        # 처리 결과 반환
        return jsonify({'result': result.stdout, 'error': result.stderr, 'output_image_path': output_image_filename})

    except Exception as e:
        return jsonify({'error': str(e)})

def convert_bytes(size_bytes):
    size_kb = size_bytes / 1024
    if size_kb < 1024:
        return f"{size_kb:.2f} KB"


if __name__ == '__main__':
    app.run(debug=True)
