
# nwgomoku
[![BuildStatus](https://travis-ci.org/nicholascw/nwgomoku.svg?branch=master)](https://travis-ci.org/nicholascw/nwgomoku)
[![GitHubLicense](https://img.shields.io/github/license/nicholascw/nwgomoku.svg)](https://github.com/nicholascw/nwgomoku/blob/master/LICENSE)
[![GitHubIssues](https://img.shields.io/github/issues/nicholascw/nwgomoku.svg)](https://github.com/nicholascw/nwgomoku/issues)
[![GitHubRepoSize](https://img.shields.io/github/repo-size/nicholascw/nwgomoku.svg)]()
[![Java](https://img.shields.io/badge/language-java-orange.svg)]()


A **gomoku** game run on terminal support single and multiplayer mode.

Created by Nicholas Wang([@nicholascw](https://github.com/nicholascw)), and distributed with GPLv3 license.
Some rights reserved.

### Obtain

##### Dependency:
    Java, Bash
    Optional:any SSH implementation, screen *(To multiplayer via internet)*
##### Build from source
```bash
$ git clone https://github.com/nicholascw/nwgomoku.git
$ cd nwgomoku/
$ javac gomoku.java
```

##### Compiled Java Class
```bash
$ wget https://github.com/nicholascw/nwgomoku/releases/[VERSION]/[VERSION].tar.gz
$ tar xzvf [VERSION].tar.gz
```

### Usage
##### To run with multiplayer mode:
```bash
$ cd /path/to/executable/
$ ./nwgomoku
```
or
```bash
$ cd /path/to/excutable/
$ java gomoku
```
##### To run with Single Player mode:
```bash
$ cd /path/to/executable/
$ ./nwgomoku single
```
or
```bash
$ cd /path/to/excutable/
$ java gomoku single
```
##### To print version:
```bash
$ cd /path/to/executable/
$ ./nwgomoku version
```
or
```bash
$ cd /path/to/excutable/
$ java gomoku version
```

### Advanced Usage
    Share your console session through SSH or telnet, then view the same session by screen's screen -x option.

