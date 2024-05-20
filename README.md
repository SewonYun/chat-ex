# kafka-exercise

jdk version: 17 temurin

---
<aside>
테스트 간소화 스크립트순서<br>
source init-devenv-container.sh @컨테이너 이미지(로컬)푸시<br>
source apply-pods.sh @쿠버네티스 팟 초기화 및 등록<br>
</aside>

---
**시나리오**

### 실시간 다자간 채팅

- 웹페이지 방문시 핑거프린트(세션) 사용(고유 id hash 6자리)
- 첫 페이지(채팅 방 목록): 30분 내 접속자 수 기준 내림차순
  - 접속자 기준 **가정** (웹소켓 ack 유지 감지)
  - 채팅방 목록 최근 메시지 노출 도착시마다 자동 최신화
- 방개설 기능(방 제목 입력 및 입장)

---

# Mono repository style git structure
<h2>Consumproducer</h2>
    Kakfa client (채팅방 개설시 새 토픽 발행)
    Kakfa client, web client(js), 컨슈밍 웹소켓 연계,
    redis session, redis sorted set
<h2>Broker</h2>
    Apache Kafaka (version),
    Clustering (Zookeeper),
    Topic (partition),
    Offset,
    Commit,
    Failover,
    Trusted event persistence (kafka default value change to  version)
<h2>Data</h2>
    Mysql, JPA transaction, Redis (채팅방 목록 캐싱 & 내림차순)

