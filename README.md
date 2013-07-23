# Irys

Irys is a java open source project on SIRI normalization projet. It's divided in differents module :
* irys-common : common classes
* irys-server : generic SIRI server including following SIRI services in Request/Response mode
  * General Messaging
  * Stop Monitoring
  * Check Status
  * Line Discovery
  * Stop Discovery
* irys-client : generic SIRI client including same services as server
* irys-client-sequencer : generic SIRI client simulating subscrption mode when server does not provide this mode
* irys-client-command : command-line mode Siri client
* webtopo-server : small web server for theorical data exchange on Neptune format
* webtopo-client : small client for theorical data exchange on Neptune format

Irys is a toolbox must be connected to an external data provider

see irys-chouette as exemple connected to a Chouette database.

Requirements
------------

* oraclejdk7
* openjdk7

External Deps
-------------
On Debian/Ubuntu/Kubuntu OS :
```sh
sudo apt-get install openjdk-7-jdk
sudo apt-get install git
```

Installation
------------

Get git repository
```sh
cd workspace
git clone -b V2_0_3 git://github.com/dryade/irys
cd irys
```

Test
----

```sh
mvn test
```

More Information
----------------

More information can be found on the [project website on GitHub](http://github.com/dryade/irys).
There is extensive usage documentation available [on the wiki](https://github.com/dryade/irys/wiki).

Example Usage
-------------

Install
```sh
mvn -Dmaven.test.skip=true install
```

License
-------

This project is licensed under the CeCILL-B license, a copy of which can be found in the [LICENSE](https://github.com/dryade/irys/blob/master/LICENSE.md) file.

Release Notes
-------------

The release notes can be found in [CHANGELOG](https://github.com/dryade/irys/blob/master/CHANGELOG.md) file

Support
-------

Users looking for support should file an issue on the GitHub issue tracking page (https://github.com/dryade/irys/issues), or file a pull request (https://github.com/dryade/irys/pulls) if you have a fix available.
=======
irys
====
