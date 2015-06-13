# hibernate-ehcache-sampleapp
A simple blogging app to demonstrate the use of ehcache/terracotta as 2nd level caching with Hibernate + Spring.

Versions tested:
- Spring 4.1.6.RELEASE
- Hibernate 4.3.10.Final
- Ehcache 2.10.0
- Terracotta OSS 4.3.0

Config files:
- jdbc.properties: DB access details
- hibernate.properties: hibernate properties + hibernate 2nd level caching details (use ehcache.xml for standalone caching, and ehcache-distributed.xml for distributed caching backed by Terracotta OSS)

Note: if enabling Distributed ehcache via Terracotta, you'll need https://github.com/lanimall/hibernate-terracotta due to a backward incompatibilities between latest ehcache and hibernate-ehcache versions.
