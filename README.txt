
Errai Quickstart Archetypes
=======================================

Build using: mvn clean install

Specify which versions of Errai the generated projects should depend on:

-Derrai.target.version= (defaults to the project version of the archetype) 
-Derrai.cdi.target.version= (defaults to errai.target.version)
-Derrai.jaxrs.target.version= (defaults to errai.target.version)

e.g. mvn clean install -Derrai.target.version=2.0.pre0 -Derrai.cdi.target.version=2.0-SNAPSHOT -Derrai.jaxrs.target.version=2.0-SNAPSHOT

Troubleshooting
=======================================

If you run into trouble don't hesitate to get in touch with us:

User Forum:     http://community.jboss.org/en/errai?view=discussions 
Mailing List:   http://jboss.org/errai/MailingLists.html
IRC:            irc://irc.freenode.net/errai

Have fun,
The Errai Team
