/** Copyright (c) 2018 kolektif Contributors
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *         http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.hhandoko.kolektif

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.reflect.runtime.universe._
import scala.util.{Failure, Success}

import io.vertx.lang.scala.json.{Json, JsonObject}
import io.vertx.lang.scala.{ScalaVerticle, VertxExecutionContext}
import io.vertx.scala.core.{DeploymentOptions, Vertx}
import org.scalatest.{AsyncWordSpec, BeforeAndAfter, MustMatchers}

// NOTE: Based on the HttpVerticleSpec template found on Vert.x Scala g8 template on
//       https://github.com/vert-x3/vertx-scala.g8/blob/master/src/main/g8/src/test/scala/%24package%24/HttpVerticleSpec.scala
abstract class VerticleBaseSpec[A <: ScalaVerticle: TypeTag]
  extends AsyncWordSpec
    with MustMatchers
    with BeforeAndAfter {

  val vertx: Vertx = Vertx.vertx

  implicit val vertxExecutionContext: VertxExecutionContext =
    VertxExecutionContext(vertx.getOrCreateContext())

  private var deploymentId = ""

  def config(): JsonObject = Json.emptyObj()

  before {
    deploymentId = Await.result(
      vertx
        .deployVerticleFuture(
          "scala:" + implicitly[TypeTag[A]].tpe.typeSymbol.fullName,
          DeploymentOptions().setConfig(config())
        ).andThen {
          case Success(d) => d
          case Failure(t) => throw new RuntimeException(t)
        },
      10.seconds
    )
  }

  after {
    Await.result(
      vertx
        .undeployFuture(deploymentId)
        .andThen {
          case Success(d) => d
          case Failure(t) => throw new RuntimeException(t)
        },
      10.seconds
    )
  }

}
