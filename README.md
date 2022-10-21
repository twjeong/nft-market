# NFT 마켓 서비스 (본인 작업 부분만 일부 발췌)
> 컨트렉트 이벤트 리스너가 SQS에 적재한 이벤트를 구독하여 마켓 DB에 변경사항 저장
- mint, createAuction, bid, buy 등 컨트렉트에서 발생시킨 모든 이벤트에 대해 entity 업데이트
- kotlin 사용
