# Blowing Bubble

## 랩세션 조교님에게 질문
* 161105 어떤 쉐이딩을 써야 journey같은 분위기가 나오는 지 물어보기.

## 다음 발표 준비

### 훈민 
* map texture mapping 구현해보기
* 바다 구현하기
* 하늘도 만들어보기
* (시간나면) 산을 피해서 목적지 까지 줄줄이 놓여있는 아이템 (충돌시 사라짐)

### 상균
* sound pressure --> 물방울의 움직임으로 바꾸기
* 버블 모델 다시 만들기 (vertice 연결 다시 해야함)
* world gravity, dargging force 구현하기
* 버블 deforming, blowing을 animation으로 만들기
* spring-mass model들어간 sphere + cube map 합치기. 

### 종민
* cube map 코드 자바로 옮기고, 알파 블랜딩 먹여보기
* 맵 충돌 처리
* (시간나면) 물방울을 따라다니는 카메라 코딩(뷰메트릭스 실시간으로 변경)
* ~~현재 glview에서 불리는 cube랑 sphere를 renderer로 옮기기~~
* ~~현재 surfaceview의 ontouchevent에 있는 collision detection도 renderer 이동 시키기~~

## 계속 준비
### 종민

### 훈민

### 상균
* 버블의 이동 trajectory를 가시화
* 시간이 지나면 버블 죽게하기? 

### 미정
* 아이템 모델 만들기
* 하늘 만들기 스카이박스

## 완료
* ~~161109 !종민 헤이즈 구현~~ 
* ~~161109 !훈민 map surface normal 보정하기~~
* ~~161109 !훈민 맵의 노멀벡터 y-axis로 가까이 모으기(?) (journey 같은 단순한 분위기)~~

## 아이디어
* journey, abzu, flower 게임 참고
* 맵을 뒤집어 위로 붙이면 동굴이 되니깐, 위로 올라가는 버블을 막을 수 있지 않을까? !훈민 그냥 위로 올라가면 기압이 낮아져서 터지게 해도?
* 소닉 게임의 링을 먹는 것처럼. 버블도 물?을 먹게해서 시간이 지나 말라서 작아지는 것을 막을 수 있게?
* 난이도를 지형의 복잡도로 결정하거나. 먹어야 할 물방울 아이템을 지형과 얼마나 가까운지로 결정.
* 나무 모델 만드는 코드 작성

## Contributors
* Hun-Min Park
* Sang-Gyun An
* JongMin Jin
