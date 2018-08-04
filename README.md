# Biome Staff (Beta)

[![CurseForge](http://cf.way2muchnoise.eu/full_biome-staff_downloads.svg)](https://minecraft.curseforge.com/projects/biome-staff)
[![Latest Version](http://cf.way2muchnoise.eu/versions/For%20MC_biome-staff_all.svg)](https://minecraft.curseforge.com/projects/biome-staff/files/latest)

## Table of Contents

* [About](#about)
* [Contact](#contact)
* [License](#license)
* [Downloads](#downloads)
* [Issues](#issues)
* [Building](#building)
* [Contribution](#contribution)
* [Maven](#maven)
* [Localization](#biome-staff-localization)
* [Credits](#credits)

## About

A mod inspired by the mod from 1.7.10 called _BiomeWand_.

Currently in BETA! I need testers! Drop me a line on [Discord](#contact) or [Twitter](#contact)

## Contact

* [Website](http://p455w0rd.net/mc/)
* [Discord #p455w0rdCraft on esper.net](http://webchat.esper.net/?channels=p455w0rdCraft&prompt=1)
* [GitHub](https://github.com/p455w0rd/BiomeStaff)
* [Twitter](https://twitter.com/TheRealp455w0rd)

## License

It's MIT..please just try and learn from it :)

## Downloads

* Downloads are available on [CurseForge](http://minecraft.curseforge.com/projects/biome-staff)

## Issues/Feature Requests

* Post 'em in the [issues](https://github.com/p455w0rd/BiomeStaff/issues) section. =D

Providing as many details as possible does help us to find and resolve the issue faster and also you getting a fixed version as fast as possible.

## Building

1. Clone this repository via 
  - SSH `git clone git@github.com:p455w0rd/BiomeStaff.git` or 
  - HTTPS `git clone https://github.com/p455w0rd/BiomeStaff.git`
2. Setup workspace 
  - Decompiled source `gradlew setupDecompWorkspace`
  - Obfuscated source `gradlew setupDevWorkspace`
  - CI server `gradlew setupCIWorkspace`
3. Build `gradlew build`. Jar will be in `build/libs`
4. For core developer: Setup IDE
  - IntelliJ: Import into IDE and execute `gradlew genIntellijRuns` afterwards
  - Eclipse: execute `gradlew eclipse`


## Contribution

* Fork -> Edit -> PR

If you are only doing single file pull requests, GitHub supports using a quick way without the need of cloning your fork. Also read up about [synching](https://help.github.com/articles/syncing-a-fork) if you plan to contribute on regular basis.

### Maven

When compiling, you can use gradle dependencies, just add

	repositories {
		maven {
			name = "covers Maven"
			url = "http://maven.covers1624.net"
		}
	}

    dependencies {
        compile "p455w0rd:BiomeStaff:<MC_VERSION>-<MOD_VERSION>"
    }
	

An example string would be `p455w0rd:BiomeStaff:1.12.2-1.0.0`

## Biome Staff Localization

### English Text

`en_us` is included in this repository, fixes to typos are welcome.

### Encoding

Files must be encoded as UTF-8.

### New Translations

I would love for someone to do translations for me =]
