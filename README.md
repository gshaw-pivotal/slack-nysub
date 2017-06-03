# NYSub Slackbot #

A slackbot that you can ask for the current status of the subway line(s); including Staten Island; in NYC managed by the MTA. This slackbot is built using Springboot.

## Getting Started ###

```
    git clone https://github.com/gshaw-pivotal/slack-nysub
```

## Integration with Slack ##

Bots and apps need to be given permission to integrate with slack. Thus you need to obtain a Slack API token for the bot. Once you have said key, place it in the application.properties file.

```
    slackBotToken=YOUR_SLACK_API_TOKEN
```

When obtaining the API token you also have to give the bot a name that will be used to address it within slack. We used 'nysub', but you may use whatever name you like.

After the slackbot is running / deployed it will not have access to any channels until invited. Bots are invited just the same as any other slack user.

## Using nysub Slackbot ##

After being invited into a slack channel, the bot can be interacted with the following commands:

1. '@nysub' results in the bot responding with instructions on how to use it.
2. '@nysub uptime' results in the bot responding with how long it has been online.
3. '@nysub status' results in the bot responding with the current reported status of all of the MTA's subway lines.
4. '@nysub status x' where x is a subway line identifier, results in the bot responding with the status of the specified subway line. This command can include multiple lines, separated by either space or comma.

When the status of a subway line is returned, it comes in the following format;

```
    [line id]: [status label] [date of status] [time of status]
    [detailed status message if present]
```

If a subway line has a status label other than `GOOD SERVICE` but the detailed status message does not directly mention said subway line, then the detailed status message is changed to;

```
    Line is not directly affected, please see {[line id]...} for more info
```

where the line id(s) listed are those that were present in the original detailed status message from the MTA.

## Supported Subway Lines ##

The bot supports all of the MTA's subway lines as at June 2017. This includes;

1, 2, 3, 4, 5, 6, 7, A, B, C, D, E, F, G, J, L, M, N, Q, R, S, SIR, W, Z

The above list is the line ids that the bot utilises and is the list users should use when asking the bot for line status.

## Notes ##

- A manifest file is present to support deployments to cloudfoundry.
- There appears to be some flakey tests in `NYSubwayStatusExtractorTest` when running `mvn clean verify`. The test pass consistently when executed via a IDE.
- There seems to be an issue where the slackbot will not be online with the slack channels it has been invited to. When slack reports this state, the bot will not respond to messages. When this has happened, our instance in PWS is executing without incident. Thus it is unclear what the root cause is. The occurrence of this 'offlining' is not consistent.