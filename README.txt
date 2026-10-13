
== LuckPermsBridge ==

A Velocity plugin that provides a raw TCP bridge to send predefined commands.
All traffic is using the US_ASCII encoding.
An example client can be found at luckperms-bridge/src/test/java/org/anax/client/ExampleClient.java
Each command has to be sent on a separate connection;

== Command Syntax ==

A command (sent raw over TCP) consists of several space-separated words:
[HASH] [TIMESTAMP] [ACTION] [ARG 1] [ARG 2] ... [ARG N];

== HASH == 

The hash must use SHA256.
It must be calculated by concatenating the command without the hash part with the secret salt.

[HASH] = SHA256("{TIMESTAMP} {ACTION} {ARG1} {ARG2} {ARG...};" + SECRET_SALT);

example:
SHA256("1791885828 SET-ROLE admin 8859ad3d-67f7-42dc-803f-cd801833348d;super-secret-salt")
  -> "726aa5d65a19482d9ea630d561a333fd6b9b9f8dd9661434b7e65a1b0ec19949"

== TIMESTAMP ==

The unix timestamp of the command request.

example: "1791885828"

== ACTION ==

Possible actions and their arguments are:

SET-ROLE <role> <uuid>
ADD-ROLE <role> <uuid>
SET-DEF-ROLE <uuid>
	- equivalent to SET-ROLE default <uuid>

MOVE-PLAYER-UUID <destination-server> <uuid> 
	- transfers the player with that uuid to a registered server with that name
MOVE-PLAYER-NAME <destination-server> <username>
	- transfers the player with that username to a registered server with that name

example: "SET-ROLE"

== ARG ==
arguments can not contain spaces nor semicolons.

examples: "8859ad3d-67f7-42dc-803f-cd801833348d",
          "admin",
          "SomePlayer__44"

== 3 EXAMPLE COMMANDS ==

8f770b39729bc40214f6ad1e9017165f45753dd9d701c5a971933e61c8d68a14 1791887234452 MOVE-PLAYER-NAME lobby SomePlayer__44;
65b7a157bc125e65074f6fdb0a125b4aa85f661a375bd285f29b084d3fbe55be 1791887424168 ADD-ROLE admin c64446c1-da27-422d-992f-e3125726c387;
17d4e4ba5aedd8dd69c09d10b929bf981806a65d7d8fdca4063b7aa71cdea994 1791887649420 SET-DEF-ROLE bdd32b63-bb7d-41cc-a2a3-5584b797211f;

(with secret-salt = "09914436e36dc52f3a80a15a99ddff7576081f6baae81dc12c641d3b9f2c784c")

== Response ==
The bridge will respond with a reponse.
A response is a single word followed by a semicolon.

possible responses are:
OK
ERR-SYN
	- syntax error (invalid command syntax)
ERR-LEN
	- the command was too long
ERR-CMD
	- the command could not be executed (for example a non existent action)
ERR-OLD
	- the timestamp was too old (or from the future)
ERR-HASH
	- the hash did not match
ERR-TIME
	- the connection timed out
ERR-SRV
	- internal server error (there was an unexpected exception)

examples: "OK;", "ERR-SYN;"

== Configuration ==

there are several configurations options in the config file.

port
	- the TCP port for listening
max-request-length
	- the maximum number of bytes the server will accept before reponding ERR-LEN and closing the connection
max-workers
	- the maximum number of connections to be handled concurrently at once.
max-request-age
	- the maximum age for a command before responding with ERR-OLD and closing the connection.
socket-timeout
	- the timeout for each connection before responding ERR-TIME and closing the connection.
secret-salt
	- the salt used to verify commands.
	- THIS SHOULD BE SECURELY RANDOM AND HAVE AT LEAST 256 BITS OF ENTROPY!!


