/*
Copyright 2019 Jigsaw Operations LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package app.intra.net.go;

import static org.junit.Assert.*;

import app.intra.net.go.TLSProbe.Result;
import org.junit.Test;

public class TLSProbeTest {

  @Test
  public void success() {
    assertEquals(Result.SUCCESS,
        TLSProbe.run(null, new String[]{"www.google.com"}));
  }

  @Test
  public void tlsFailed() {
    assertEquals(Result.TLS_FAILED,
        TLSProbe.run(null, new String[]{"untrusted-root.badssl.com"}));
    assertEquals(Result.TLS_FAILED,
        TLSProbe.run(null, new String[]{"self-signed.badssl.com"}));
  }

  @Test
  public void otherFailedDNS() {
    assertEquals(Result.OTHER_FAILED,
        TLSProbe.run(null, new String[]{"bad-domain.bad-tld"}));
  }

  @Test
  public void otherFailedEmpty() {
    assertEquals(Result.OTHER_FAILED,
        TLSProbe.run(null, new String[]{"dns.google:53"}));
  }
}
