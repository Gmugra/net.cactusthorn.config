/*
* Copyright (C) 2021, Alexei Khatskevich
*
* Licensed under the BSD 3-Clause license.
* You may obtain a copy of the License at
*
* https://github.com/Gmugra/net.cactusthorn.config/blob/main/LICENSE
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
* FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
* DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
* SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
* CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
* OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
* OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package net.cactusthorn.config.core.loader;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;

import org.junit.jupiter.api.Test;

public class UriTemplateTest {

    @Test public void nocacheStr() {
        Loaders.UriTemplate template = new Loaders.UriTemplate("nocache:system:properties");
        assertFalse(template.cachable());
        assertEquals(URI.create("system:properties"), template.uri());
    }

    @Test public void nocacheURI() {
        Loaders.UriTemplate template = new Loaders.UriTemplate(URI.create("nocache:system:properties"));
        assertFalse(template.cachable());
        assertEquals(URI.create("system:properties"), template.uri());
    }

    @Test public void cacheStr() {
        Loaders.UriTemplate template = new Loaders.UriTemplate("system:properties");
        assertTrue(template.cachable());
        assertEquals(URI.create("system:properties"), template.uri());
    }

    @Test public void cacheURI() {
        Loaders.UriTemplate template = new Loaders.UriTemplate(URI.create("system:properties"));
        assertTrue(template.cachable());
        assertEquals(URI.create("system:properties"), template.uri());
    }
}
