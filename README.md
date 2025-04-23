# 概要
SpringAIを使用して、ローカルで起動している生成AIとチャットする。
MCPサーバーも作成してみる。

# 前提条件
ローカルではOllamaを使用して、```deepseek-r1:8b```を起動していること。

# 使用手順
1. maven install を実行
2. Spring を起動
3. エンドポイントをたたく
回答例）http://localhost:8080/ai/simple?message=hello
````json
{"completion":"<think>\nOkay, so I need to figure out how to respond to this message. The user wrote \"hello\" and then said they're a friendly chatbot that answers questions in the voice of a pirate.\n\nFirst, I should understand what the user is asking for. They want me to act like a pirate when responding, but also keep it friendly. Maybe they're looking for a fun and engaging interaction.\n\nI should start with a greeting that's pirate-themed. Words like \"Arrr\" or \"Aye\" might be good. Then, perhaps acknowledge their message and ask how I can assist them today in a lighthearted way.\n\nI need to make sure the tone stays positive and doesn't come off as too serious. Using phrases like \"matey\" could help keep it casual. Also, ending with something encouraging, like telling them to ask away or suggesting they might find some treasures, would be fun.\n\nLet me put that together: start with a pirate greeting, mention their message, ask how I can help today, and add a bit of humor about treasure or adventure.\n\nI think that covers the key points. Now, let's make sure it flows naturally without any awkward parts.\n</think>\n\nArrr, matey! Ahoy there, ye scurvy dog! Did ye think to wake the beast with yer \"hello\"? Well, ye done it, mate! How can a pirate like me be of service to thee today? Avast! Ask away, and may the winds be fair!"}
````

# MCPサーバーの設定方法
## ビルドの実行
````
./mvnw clean install -DskipTests
````
## MCPクライアントに設定
````
{
  "mcpServers": {
    "spring-ai-mcp-weather": {
      "command": "java",
      "args": [
        "-Dspring.ai.mcp.server.stdio=true",
        "-Dspring.main.web-application-type=none",
        "-Dlogging.pattern.console=",
        "-jar",
        "/absolute/path/to/mcp-weather-stdio-server-0.0.1-SNAPSHOT.jar"
      ]
    }
  }
}
````

## 今後の予定
スケジュールの検索や登録ができるMCPサーバーを、作成する
