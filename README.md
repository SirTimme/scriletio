<h1 align="center">Scriletio</h1>

<div align="center">
    <img src="https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white" alt="docker badge"/>
    <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" alt="postgresql badge"/>
    <img src="https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white" alt="discord badge"/>
    <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white" alt="gradle badge"/>
</div>

<h3 align="center">Scriletio enables you to delete messages<br/> in your discord server after a configured duration.</h3><br>

## Table of contents

1. [Commands](#commands)
2. [Self-hosting](#self-hosting)

## Commands

### Overview

| Command                                             | Task                                            |
|-----------------------------------------------------|-------------------------------------------------|
| [/register](#register)                              | Registers yourself in the database              |
| [/autodelete add channel duration](#autodelete-add) | Adds an autodelete config for the specified channel |

### /register

> [!NOTE]
> | Parameter | Type | Required |
> | -- | -- | -- |
> | - | - | - |

Example:

![register command](src/main/resources/assets/register_command.png)

### /autodelete add

> [!NOTE]
> | Parameter | Type | Required |
> | -- | -- | -- |
> | channel | Textchannel | true |
> | duration | String | true |

Example:

![autodelete add command](src/main/resources/assets/autodelete_add_command.png)

## Self-hosting

