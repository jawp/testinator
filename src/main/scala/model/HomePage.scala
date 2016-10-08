package model

import scala.xml.NodeSeq

object HomePage {
  def content: NodeSeq =
    <html>
      <head>
        <title>Unattended Programming Test Instructions</title>
        <style type="text/css">
          table
          &#123;
          border-collapse: collapse;
          margin: 10px;
          &#125;

          td, th
          &#123;
          border: thin solid black;
          padding: 5px;
          &#125;

          em
          &#123;
          text-decoration: underline;
          &#125;
        </style>
      </head>
      <body>
        <h1>Unattended Programming Test Instructions</h1>

        <p>
          You will write an application to answer questions. The questions will be supplied from our
          <a href="/">external webservice</a>
          (i.e. this website), which will also check your answers.
          You should interact with the web service using HTTP GET calls (I know, not truly RESTful) from your Scala code.
          Your goal is to write an application that when run, automatically and correctly answers 10 questions in a row.
          When your application can do that, zip up the entire project and send it to us.
          We will run it locally to verify that it works,
          and we will review the code and tests.
        </p>

        <h3>Interactions follow a simple cycle:</h3>
        <table>
          <thead>
            <tr>
              <th>#</th>
              <th>URL</th>
              <th>Purpose</th>
              <th>Response</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>1</td>
              <td>/startTest/[your name]</td>
              <td>Tell the service you want to begin a session</td>
              <td>A session token</td>
            </tr>
            <tr>
              <td>2</td>
              <td>[token]/nextQuestion</td>
              <td>Ask for the next question</td>
              <td>A question</td>
            </tr>
            <tr>
              <td>3</td>
              <td>[token]/answer/[answer]</td>
              <td>Provide the answer</td>
              <td>"pass" or "fail"</td>
            </tr>
          </tbody>
        </table>
        <h3>If your answer is:</h3>
        <ul>
          <li>
            <em>Correct</em>
            , you will receive a response saying "pass". Return to step (2)</li>
          <li>
            <em>Wrong</em>
            , you will receive a response saying "fail". You will need to get a new token and start again.
            <em>All subsequent requests</em>
            for the
            current token will return the text "Spoilt scorecard. Please start again."
          </li>
          <li>
            <em>The last</em>
            of 10 correct answers, the next question (the 11th) will contain the text "You have finished"
          </li>
        </ul>

        <h3>Example Code</h3>

        <p>
          You may find it useful to see some example HTTP client code. Here is an example Scala method that fetches data
          from a port on localhost. It uses the naive-http HTTP client library, which you can find bundled with a nice
          simple HTTP server
          <a href='https://github.com/timt/naive-http-server'>here</a>
        </p>
        <pre>
          import io.shaka.http.Http.http
          import io.shaka.http.Request.GET

          private def get(requestPath: String) = http(GET(s"http://localhost:$httpPort$requestPath")).entityAsString
        </pre>
        <h3>Please Note:</h3>
        <ul>
          <li>You will be graded on the quality of your code and your tests</li>
          <li>You do not have to use the naive-http client if you have another preferred method</li>
          <li>Writing simple, elegant code will earn more points than using lots of frameworks</li>
          <li>You can use any test framework you like (e.g. specs2, scalatest)</li>
        </ul>
      </body>
    </html>
}