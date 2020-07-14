package com.higherkingpud.twitterclone2.infra.mysql

import com.higherkingpud.twitterclone2.domain.entities.TweetId
import com.higherkingpud.twitterclone2.domain.repositories.TweetRepository
import scalikejdbc._

import scala.concurrent._

class TweetRepositoryOnMySQL(
  session: DBSession, 
  executionContext: ExecutionContext
) extends TweetRepository {
  import TweetRepository._
  implicit val s = session
  implicit val ec = executionContext
  
  val columns = TweetSchema.column

  def create(tweet: TweetCreating): Future[Unit] = Future {
    withSQL {
      insert.into(TweetSchema).namedValues(
        columns.id -> tweet.id.toString,
        columns.userId -> tweet.userId,
        columns.text -> tweet.text
      )
    }
  }

  def drop(tweetId: TweetId): Future[Unit] = Future {
    applyUpdate {
      delete.from(TweetSchema).where.eq(columns.id, tweetId.toString)
    }
  }
}
