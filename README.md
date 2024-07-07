<h1 align="center">Scriletio</h1>

<div align="center">
    <img src="https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white" alt="docker badge"/>
    <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" alt="postgresql badge"/>
    <img src="https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white" alt="discord badge"/>
    <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white" alt="gradle badge"/>
</div>

<h3 align="center">Scriletio is a discord bot that automatically <br/> deletes your messages after a configured duration.</h3><br>

## Table of contents

1. [Commands](#commands)
2. [Self-hosting](#self-hosting)

## Commands

### Overview

| Command                                                                          | Task                                                           |
|----------------------------------------------------------------------------------|----------------------------------------------------------------|
| [/register](#register)                                                           | Registers yourself in the database                             |
| [/autodelete add \<channel> \<duration>](#autodelete-add-channel-duration)       | Adds an autodelete config for the specified channel            |
| [/autodelete get](#autodelete-get)                                               | Displays an overview for all autodelete configs in this server |
| [/autodelete update \<channel> \<duration>](#autodelete-update-channel-duration) | Updates an existing config with the specified duration         |
| [/autodelete delete](#autodelete-delete)                                         | Deletes an existing autodelete config                          |

---

### /register

> [!NOTE]
> | Parameter | Type | Required |
> | -- | -- | -- |
> | - | - | - |

Example:

![register command](src/main/resources/assets/register_command.png)

---

### /autodelete add \<channel> \<duration>

> [!NOTE]
> | Parameter | Type | Required |
> | -- | -- | -- |
> | channel | Textchannel | true |
> | duration | String | true |

Example:

![autodelete add command](src/main/resources/assets/autodelete_add_command.png)

---

### /autodelete get

> [!NOTE]
> | Parameter | Type | Required |
> | -- | -- | -- |
> | - | - | - |

Example:

![autodelete get command](src/main/resources/assets/autodelete_get_command.png)

---

### /autodelete update \<channel> \<duration>

> [!NOTE]
> | Parameter | Type | Required |
> | -- | -- | -- |
> | channel | Textchannel | true |
> | duration | String | true |

Example:

![autodelete update command](src/main/resources/assets/autodelete_update_command.png)

> [!IMPORTANT]
> The choices for **channel** are dynamically resolved based on your saved autodelete configs.\
> You need to use one of the provided choices to update an autodelete config!
>
> ![autodelete update channel choices](src/main/resources/assets/autodelete_update_channel_choices.png)

---

### /autodelete delete

## Self-hosting

