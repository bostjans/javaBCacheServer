[![CI-Maven-BCacheServer](https://github.com/bostjans/javaBCacheServer/actions/workflows/maven.yml/badge.svg)](https://github.com/bostjans/javaBCacheServer/actions/workflows/maven.yml)

# javaBCacheServer
Java (B)Cache server > to serve/centralize cache.


# Download

Look in Releases.


# Test run

```
docker run --name bCacheSrv01 --rm --cpus=2 -m 1512m \
  -p 13111:13111 -p 13112:13112 \
  --restart=unless-stopped \
  bostjans/bcacheserver:latest
```
To specify cache server Hostname:
```
docker run --name bCacheSrv01 --rm --cpus=2 -m 1512m \
  -p 13111:13111 -p 13112:13112 \
  --restart=unless-stopped \
  --add-host=cachesrv01:10.11.15.13 \
  -e ENV_RMI_HOSTNAME='cachesrv01' \
  -e JAVA_TOOL_OPTIONS="-Djava.rmi.server.hostname=cachesrv01" \
  bostjans/bcacheserver
```



# Reference

* https://cache2k.org/benchmarks.html
* https://cruftex.net/2017/09/01/Java-Caching-Benchmarks-Part-3.html
* https://infinispan.org/blog/tags/benchmarks/
* https://redis.io/topics/benchmarks
* https://docs.keydb.dev
* https://dragonflydb.io