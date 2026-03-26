# roka-calendar

## 목적
군 복무 진행률을 계산해 4:1 비율 PNG 이미지를 생성하는 안드로이드 앱.
인스타그램 스토리 스티커 용도.

## 기술 스택
- Kotlin 2.0 + Jetpack Compose + Material 3
- Gradle 8.10.2, AGP 8.7.3
- minSdk 26, targetSdk 35
- 단일 Activity 구조 (오버엔지니어링 금지)

## 핵심 파일
| 파일 | 역할 |
|------|------|
| `ServiceProgress.kt` | 복무 기간/진행률 계산 모델 |
| `StickerBitmapFactory.kt` | 4:1 PNG 비트맵 생성 |
| `StickerImageExporter.kt` | MediaStore 저장 + 클립보드 복사 |
| `RokaCalendarApp.kt` | 앱 전체 상태 관리 컴포저블 |
| `RokaStickerScreen.kt` | UI 화면 컴포저블 |

## 빌드
```bash
./gradlew assembleDebug
```
※ Android SDK 필요 (ANDROID_HOME 환경 변수 또는 local.properties 설정)
