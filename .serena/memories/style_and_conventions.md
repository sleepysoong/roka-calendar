# 코드 스타일

## Kotlin
- ktfmt/ktlint 표준 스타일
- trailing comma 사용
- 들여쓰기 4칸
- 파일 끝 개행

## Compose
- 상태는 가능하면 `rememberSaveable` 사용
- 이벤트 콜백은 `on...Click` 형태로 네이밍
- 미리보기용 `@Preview` 함수는 필요 시 추가

## 패키지 구조
```
com.sleepysoong.rokacalendar
├── model/          # 데이터 클래스
├── image/          # 이미지 생성 유틸
├── share/          # 저장/공유 유틸
└── ui/             # Compose UI
    └── theme/
```
