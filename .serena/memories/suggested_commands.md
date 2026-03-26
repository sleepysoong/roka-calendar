# 개발 명령어

## 빌드
```bash
./gradlew assembleDebug      # 디버그 APK 빌드
./gradlew assembleRelease    # 릴리스 APK 빌드
./gradlew clean              # 빌드 캐시 삭제
```

## 검증
```bash
./gradlew :app:compileDebugKotlin   # Kotlin 컴파일만
./gradlew lint                       # Android Lint 실행
```

## 환경 요구사항
- JDK 17+
- Android SDK (ANDROID_HOME 설정 필요)
