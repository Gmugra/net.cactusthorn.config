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
package net.cactusthorn.config.tests.converter;

import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.Default;
import net.cactusthorn.config.core.converter.ConverterClass;

@Config public interface ConfigConverter {

    @ConverterClass(CustomURIConverter.class) @Default("https://github.com") URI url();

    @ConverterClass(CustomURIConverter.class) Optional<URI> ourl();

    @ConverterClass(CustomURIConverter.class) Optional<List<URI>> listURI();

    Optional<List<Path>> listPath();

    @Default("https://github.com") URI defaultConverter();

    @ConverterClass(MyInterfaceConverter.class) Optional<MyInterface> myInterface();

    @ConverterClass(MyInterfaceConverter.class) Optional<List<MyInterface>> myInterfaceList();

    @ConverterClass(MyAbstractClassConverter.class) Optional<MyAbstractClass> myAbstractClass();

    @ConverterClass(MyAbstractClassConverter.class) Optional<List<MyAbstractClass>> myAbstractClassList();

    Optional<Map<URL, URI>> defaulConvertersMap();

    Optional<Map<Path, URI>> defaulConvertersPathMap();

    Optional<Map<URI, Path>> defaulConvertersPathMap2();

    @ConverterClass(MyInterfaceConverter.class) Optional<Map<String, MyInterface>> myInterfaceMap();

    @ConverterClass(MyInterfaceConverter.class) Optional<Map<Path, MyInterface>> myInterfaceMap2();

    @ConverterClass(MyAbstractClassConverter.class) Optional<Map<String, MyAbstractClass>> myAbstractClassMap();

    @ConverterClass(MyAbstractClassConverter.class) Optional<Map<Path, MyAbstractClass>> myAbstractClassMap2();
}
