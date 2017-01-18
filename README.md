# KillRanking

*Next updates*
- [x] implement MySQL
- [x] Cache improve.
- [ ] Remove flatfiles.
- [ ] Clear the code.


###Download the last release
[0.0.3](https://github.com/JonasXPX/KillRanking/releases)

###make sure if your config.yml has these codes
```yaml
enable_mysql: false to flatfile | true for mysql
mysql:
  host: localhost:3306
  database: KillRanking
  username: root
  password: hackme
```

###Warning!!! if you are using the flatfile, I'll remove it in next release.

To pass all user of the flatfile to MySQL, make sure if your config.yml has this line

```yaml
enable_mysql: true
```