# Công cụ dùng cho dự án
- ecj 3.26.0 (biên dịch hook -source 1.3 -target 1.3): https://repo1.maven.org/maven2/org/eclipse/jdt/ecj/3.26.0/ecj-3.26.0.jar -> tools/ecj.jar
- Bộ tool NAM.P (fonts f13/f15/f18 + c/s, BitmapFont.class, pixeltext-kit, freej2me_plus.jar): giải nén bằng
  `bash scripts/setup_tools.sh Tool_Viet_Hoa_NAM_P.zip work` -> work/tools (cùng CFR 0.152 + ProGuard 7.4.2 tải từ GitHub/Maven).
- JDK 21 đầy đủ (cần jmods/java.base.jmod cho preverify/ProGuard).
