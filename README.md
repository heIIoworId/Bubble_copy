# Blowing Bubble

## 랩세션 조교님에게 질문

## 게임 진행 방법
* 맵 곳곳에 위치한 아이템(물방울)을 다 먹기
* 아이템을 오랫동안 먹지 못하면, 방울이 말라서 터짐
* 얼마나 빨리 모든 아이템을 먹었는지 시간을 기록 --> 경쟁
* 난이도 조절: 아이템 개수, 맵 복잡도
* 맵 안/밖의 경계를 어떻게 시각적으로 표시할 것인가? 맵 큐브 밖은 위험해(용암 square 바닥에 깔기)
* 맵 밖으로 나가면 어떻게? 방울이 더 빨리 마르게 하기(방울 빨간색)
* 너무 높아지더라도 방울이 빠르게 마르게 하기(방울 파란색)

## 마지막 발표 준비

### 리스트
* 렌더 에러 고치기?
* 게임 시작, 게임 오버 정도의 화면? 
* 진동?? 죽을 떄?

### 훈민 
* 노멀 매핑 일단 구현하고, tri-planar랑 합칠지 생각해보기
* 버블 위치도 산을 피해서 놓기
* 투명한 바다 밑에 보일 바닥 채워 넣기(검은색 사각형)
* 맵크기 journey game 참고해서 키우기
* 터치 했을 떄 카메라 뷰 바꾸는거 막기
* 구름?

### 상균
* 아이템 - 버블 충돌 문제 (아이템 위치를 잘 확인해보기)
* (다음에) 남은 아이템 개수 표시 (카메라 앞에 rect 만들어서 mapping하거나, 안드로이드 viewOverlay 활용 생각, 재귀적으로 자신을 부르는 함수?)
* 마이크에서 인식하는 진동수 제한?
* ~~메인 엑티비티에서 마이크 에러 on pause 안에 넣어서 고치기~~
* ~~bgm 넣기~~
* 버블 동선 시각화 개선 (리본) (불필요?)

### 종민
* (오차 누적? 아크볼? quaternion?) 카메라 회전이 커지는 문제 고치기, 중력 방향으로 자이로 초기값 설정? 
* 스카이 박스(우주?) 및, 바다 텍스쳐 이미지 화질 좋은 것으로 갈아 끼우기.
* 우리 레퍼런스 폰에 설치해서 가기  


### 미정
* journey, abzu, flower 게임 참고

## 완료
* 161119
* ~~방울을 따라다니는 카메라 코딩(뷰메트릭스 실시간으로 변경)~~
* ~~현재 glview에서 불리는 cube랑 sphere를 renderer로 옮기기~~
* ~~현재 surfaceview의 ontouchevent에 있는 collision detection도 renderer 이동 시키기~~
* ~~cube map 코드 자바로 옮기고, 알파 블랜딩 먹여보기~~
* ~~sound pressure --> 물방울의 움직임으로 바꾸기~~
* ~~버블 모델 다시 만들기 (vertice 연결 다시 해야함)~~
* ~~world gravity, dargging force 구현하기~~
* ~~버블 deforming, blowing을 animation으로 만들기~~
* ~~spring-mass model들어간 sphere + cube map 합치기~~ 
* ~~map texture mapping 구현해보기~~
* ~~바다 구현하기~~
* ~~하늘도 만들어보기~~

* 161109
* ~~!종민 헤이즈 구현~~ 
* ~~!훈민 map surface normal 보정하기~~
* ~~!훈민 맵의 노멀벡터 y-axis로 가까이 모으기(?) (journey 같은 단순한 분위기)~~
