# f64_image_upscaler/inference_realesrgan.py

import argparse
import cv2
import glob
import os
from basicsr.archs.rrdbnet_arch import RRDBNet
from realesrgan import RealESRGANer
from gfpgan import GFPGANer  # 얼굴 개선 모델 추가

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('-i', '--input', type=str, required=True, help='Input image path')
    parser.add_argument('-o', '--output', type=str, default='results', help='Output folder')
    parser.add_argument('-s', '--outscale', type=float, default=4, help='Upscaling scale (default: 4x)')
    parser.add_argument('--face_enhance', action='store_true', help='Enable face enhancement with GFPGAN')
    parser.add_argument('--ext', type=str, default='png', choices=['png', 'jpg'], help='Output format')
    parser.add_argument('--gpu-id', type=int, default=None, help='GPU ID to use (default: None)')

    args = parser.parse_args()

    # 모델 경로 설정 (RealESRGAN & GFPGAN)
    model_path = 'gfpgan/weights/RealESRGAN_x4plus.pth'
    face_model_path = 'gfpgan/weights/GFPGANv1.3.pth'

    # RealESRGAN 모델 로드
    model = RRDBNet(num_in_ch=3, num_out_ch=3, num_feat=64, num_block=23, num_grow_ch=32, scale=4)
    upsampler = RealESRGANer(
        scale=4,
        model_path=model_path,
        model=model,
        tile=0,
        tile_pad=10,
        pre_pad=0,
        half=True,  # fp16
        gpu_id=args.gpu_id
    )

    # 얼굴 개선 (GFPGAN) 모델 로드
    if args.face_enhance:
        face_enhancer = GFPGANer(
            model_path=face_model_path,
            upscale=args.outscale,
            arch='clean',
            channel_multiplier=2,
            bg_upsampler=upsampler
        )

    os.makedirs(args.output, exist_ok=True)

    # 파일 하나만 처리하는 로직
    imgname, extension = os.path.splitext(os.path.basename(args.input))
    print(f'Processing: {imgname}{extension}')

    img = cv2.imread(args.input, cv2.IMREAD_UNCHANGED)
    if img is None:
        print(f'Error: Failed to load image {args.input}')
        return

    # 얼굴 개선 적용 여부
    if args.face_enhance:
        _, _, output = face_enhancer.enhance(
            img, has_aligned=False, only_center_face=False, paste_back=True
        )
    else:
        output, _ = upsampler.enhance(img, outscale=args.outscale)

    # 저장 경로 설정
    save_path = os.path.join(args.output, f'{imgname}_upscaled.{args.ext}')
    cv2.imwrite(save_path, output)
    print(f'Saved: {save_path}')

if __name__ == '__main__':
    main()
