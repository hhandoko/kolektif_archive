/** File     : MainVerticleSpec.scala
  * License  :
  *   Copyright (c) 2018 kolektif Contributors
  *
  *   Licensed under the Apache License, Version 2.0 (the "License");
  *   you may not use this file except in compliance with the License.
  *   You may obtain a copy of the License at
  *
  *           http://www.apache.org/licenses/LICENSE-2.0
  *
  *   Unless required by applicable law or agreed to in writing, software
  *   distributed under the License is distributed on an "AS IS" BASIS,
  *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  *   See the License for the specific language governing permissions and
  *   limitations under the License.
  */
package com.hhandoko.kolektif

import scala.concurrent.Promise

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MainVerticleSpec
  extends VerticleBaseSpec[MainVerticle] {

  "MainVerticle" should {

    "return 'Hello from Vert.x' on '/'" in {
      val promise = Promise[String]

      vertx
        .createHttpClient()
        .getNow(8080, "127.0.0.1", "/", res => {
            res.exceptionHandler(promise.failure)
            res.bodyHandler { body => promise.success(body.toString) }
        })

      promise.future.map { res =>
        res mustEqual "Hello from Vert.x"
      }
    }
  }

}
