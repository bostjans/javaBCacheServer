[![CI-Maven-BCacheServer](https://github.com/bostjans/javaBCacheServer/actions/workflows/maven.yml/badge.svg)](https://github.com/bostjans/javaBCacheServer/actions/workflows/maven.yml)

# javaBCacheServer
Java (B)Cache server > to serve/centralize cache.


# Download

Look in Releases.


# Test run

```
docker run --name bCacheSrv01 --rm --cpus=2 -m 1512m --memory-reservation=956m \
  -p 13111:13111 -p 13112:13112 \
  bostjans/bcacheserver:0.6.8
```


# Reference

* https://cache2k.org/benchmarks.html
* https://cruftex.net/2017/09/01/Java-Caching-Benchmarks-Part-3.html
* https://infinispan.org/blog/tags/benchmarks/
* https://redis.io/topics/benchmarks
* https://docs.keydb.dev
* https://dragonflydb.io