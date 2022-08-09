# Crypto Finder

Description
--------------------

Trending cryptocurrency tracker to find out the cryptocurrencies that are trending on the Binance exchange.

Java Spring Boot microservice deployed on the AWS through Elastic Beanstalk that regularly polled the exchange for cryptocurrency pairs' information. 
The microservice would send email notifications with the cryptocurrencies that increased with respect to the dollar by a percentage in a given timeframe. 
The percentage and the timeframe are configurable via the following properties:
- cryptoFinder.percent.price.increase
- binance.poll.interval.ms

For example, if cryptocurrency X had a 20% increase in the last 10 minutes, the subscribers would receive an email alert with that information.

Tech Stack
--------------------
- Java 11
- Spring Boot
- AWS Elastic BeanStalk