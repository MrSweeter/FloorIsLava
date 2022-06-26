
# FloorIsLava

[Spigot](https://www.spigotmc.org/resources/the-floor-is-lava.43250/)

![Spigot page](https://i.imgur.com/vjMi1Qq.png)

<details>
  <summary>lang.yml</summary>
  
    #Empty configuration section
    ECS: "Zone <{NAME}> not loaded, empty configuration section"
    #Load zone complete
    LZC: "Zone <{NAME}> load complete"
    #Game already start
    GAS: "§cParty is already running"
    #Game access
    GA: "§6You enter in a party"
    #Already in party
    AIP: "§cYou are already in a party"
    #Use flleave to quit
    UFLQ: "§cYou must quit your party to execute this action !, /flleave"
    JP: §9§lJoin party !
    #Click here to join
    CHJ: "§fClick here to join a party"
    #Ready
    R: "§9§lReady ?"
    #Click here to set ready
    CHSR: "§fClick here to say you are ready"
    # 0/0 Player ready
    PR: "§e{VALUE} §6players ready !"
    # 0/0 player in party
    PP: "§e{VALUE} §6players in party"
    MBR: "§cYou must move between each lava flow !"
    #secondes before first round
    SBFR: "§b{TIME} §6secondes before the first lava flow"
    #secondes before next round
    SBNR: "§b{TIME} §6secondes before the next lava flow"
    #player wait you for party
    PWFP: "§b{PLAYER} §6wait you to join a party, §a/fltp {PARTY}"
    #No party named
    NPN: "§cNo party with this name"
    #Death in party
    DIP: "§b{PLAYER} §6is death in lava flow"
    #Quit party
    QP: "§b{PLAYER} §6leave the party"
    #End game not enough player
    ENNOP: "§cEnd of party, not enough player"
    #You quit party
    YQP: "§6You leave the party"
    #Already ready
    AR: "§cYou are already ready"
    #Not your party
    NYP: "§cIt's not your party"
    #player is ready
    PIR: "§6Player §b{PLAYER} §6is ready !"

</details>

<details>
  <summary>zones.zml</summary>
    
    noctabato:
      name: §9§lShip of Nocta
      world: sweetdream
      game-manager:
        x: 2.5
        y: 22.0
        z: -78.5
        yaw: 90
        pitch: 0
      teleport-to-manager:
        x: -1.5
        y: 22.0
        z: -78.5
        yaw: 270
        pitch: 0
      teleport-to-arena:
        x: -17.5
        y: 17.0
        z: -78.5
        yaw: 90
        pitch: 0
      corner-1:
        x: -32
        y: 16
        z: -90
      corner-2:
        x: 54
        y: 30
        z: -68
      floor:
      - WOOD-1
      - QUARTZ_BLOCK
      - WOOD_STEP-9
      - GRASS
      - STATIONARY_WATER
      - WATER

</details>

 [Youtube](https://youtu.be/Zd3rwXeaz6Q)

<details>
  <summary>Demo map</summary>
Thanks to Noctageek for the boat
  
![DemoMap](https://i.imgur.com/9DkQ1ZT.png)
  
Map + zones.yml configuration [here](http://www.mediafire.com/file/rsbpigioyhcloqx/TheFloorIsLava.zip)  
</details>

Not tested in anterior version of 1.12, but can works, if you test it in anterior version with no problem, you can tell it in discussion

