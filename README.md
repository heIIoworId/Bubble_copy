# Blowing Bubble

## 랩세션 조교님에게 질문
* 어느 순간 카메라 y 축이 뒤집히는 문제 해결(gimbal lock, euler angles?? --> quaternion?)

## 게임 진행 방법
* 맵 곳곳에 위치한 아이템(물방울)을 다 먹기
* 아이템을 오랫동안 먹지 못하면, 방울이 말라서 터짐
* 얼마나 빨리 모든 아이템을 먹었는지 시간을 기록 --> 경쟁
* 난이도 조절: 아이템 개수, 맵 복잡도
* 맵 안/밖의 경계를 어떻게 시각적으로 표시할 것인가? 맵 큐브 밖은 위험해(용암 square 바닥에 깔기)
* 맵 밖으로 나가면 어떻게? 방울이 더 빨리 마르게 하기(방울 빨간색)
* 너무 높아지더라도 방울이 빠르게 마르게 하기(방울 파란색)

## 다음 발표 준비

### 훈민 
* bump mapping (normal mapping) (tri-planar와 조합하기)
* 산을 피해서 스피어(아이템) 놓기
* 화면을 손가락으로 눌렀을 떄 맵을 순간적으로 투명하게
* ~~tri-planar texture mapping~~
* ~~맵 사이즈 키우기(x4)~~
* ~~스카이박스 넓이로 용암 깔기~~

### 상균
* 남은 아이템 개수 표시 (쓰레드 새로 만들어서, 버튼 텍스트 업데이트)
* 시간 지나면 버블 사이즈 줄어들게 하기, 아이템 먹으면 다시 커지게 하기
* (시간나면) 버블의 이동 trajectory를 가시화 (BUBBLE CORE'S PAST LOCATION ARRAY?)(display only when bubble is moving)(투명한 리본)
* 맵 밖으로 나가면 버블 색깔 바꾸기
* media recorder 화면 켤 때 멈추는 거 해결하기 (state관리가 포인트)
* ~~global boolean 으로 soundhandler on/off 처리~~
* ~~아이템 모델링하기, 아이템은 충돌시 사라짐~~
* ~~아이템 다 먹었는지 확인, 최종 시간 기록 구현(gameManager class)~~

### 종민
* 버블-카메라 사이 거리 더 키우기
* 버블-맵 충돌 처리
* 버블-스피어(아이템) 충돌 처리
* gyro 각도 90도에 view 180도 매핑(2배로 더 돌게)
* 어느 순간 카메라 y 축이 뒤집히는 문제 해결(gimbal lock, euler angles?? --> quaternion?)
* 스카이박스 평범한 하늘 사진으로 바꾸기

### 미정
* 효과음(bubble) 및 배경음 넣기
* journey, abzu, flower 게임 참고
* 진동 넣기?

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
