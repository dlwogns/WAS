-- KEYS[1] = queue key (예: family:123:syncQueue)
-- KEYS[2] = processing set key (예: family:123:processing)
-- ARGV[1] = 메시지 expire TTL (초)

local item = redis.call("RPOP", KEYS[1])
if item then
  redis.call("SADD", KEYS[2], item)
  redis.call("EXPIRE", KEYS[2], ARGV[1])
end
return item
