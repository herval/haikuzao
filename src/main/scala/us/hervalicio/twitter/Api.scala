package us.hervalicio.twitter

import twitter4j.conf.ConfigurationBuilder
import twitter4j.{Status, Query, TwitterFactory}
import scala.collection.JavaConversions._

trait Api extends ApiConfig {

  val config = new ConfigurationBuilder()
  config.setDebugEnabled(true)
      .setOAuthConsumerKey(consumerKey)
      .setOAuthConsumerSecret(consumerSecret)
      .setOAuthAccessToken(accessToken)
      .setOAuthAccessTokenSecret(accessTokenSecret)

  val factory = new TwitterFactory(config.build())
  val twitter = factory.getInstance()


  def post(message: String) = {
    twitter.updateStatus(message)
  }
  
}
