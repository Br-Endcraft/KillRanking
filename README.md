# KillRanking

*Next updates*
- [x] implement MySQL
- [x] Cache improve.
- [ ] Remove flatfiles.
- [ ] Clear the code.

###LegendChat
make sure you have the [LegendChat](http://dev.bukkit.org/bukkit-plugins/legendchat/).
add the key in config.yml of the LegendChat
```yaml
format:
  default: ........ {killranking} .........
```


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

To move all users of the flatfile to MySQL, make sure if your config.yml has this line

```yaml
enable_mysql: true
```

##config.yml example
```yaml
ranks:
   myNewRank1:
      Nome: NewRank
      #Ever the first rank need to set 0 in MinKills
      MinKills: 0
      MaxKills: 100
      Tag: '&a[NewRank]'
   anotherRank2:
      Nome: User
      MinKills: 101
      MaxKills: 150
      Tag: '&f[User]'
      # key Comandos is Optional.
      Comandos: 
        - 'say We have a new User @player'
        - 'money give @player 100'
        
```