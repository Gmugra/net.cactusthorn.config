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
package net.cactusthorn.config.compiler;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

public interface GeneratorPart {

    ClassName LIST = ClassName.get(List.class);
    ClassName SET = ClassName.get(Set.class);
    ClassName MAP = ClassName.get(Map.class);
    ClassName CONCURRENTHASHMAP = ClassName.get(ConcurrentHashMap.class);

    ClassName OBJECT = ClassName.get(Object.class);
    ClassName STRING = ClassName.get(String.class);

    TypeName SET_STRING = ParameterizedTypeName.get(SET, STRING);
    TypeName MAP_STRING_OBJECT = ParameterizedTypeName.get(MAP, STRING, OBJECT);
    TypeName MAP_STRING_STRING = ParameterizedTypeName.get(MAP, STRING, STRING);
    TypeName CONCURRENTHASHMAP_STRING_OBJECT = ParameterizedTypeName.get(CONCURRENTHASHMAP, STRING, OBJECT);

    String VALUES_ATTR = "VALUES";
    String URIS_ATTR = "URIS";
    String INITIALIZER_ATTR = "INITIALIZER";

    String HASH_CODE_ATTR = "hashCode";
    String CALCULATE_HASH_CODE_METHOD = "calculate__Hash__Code";

    String TO_STRING_ATTR = "toString";
    String GENERATE_TO_STRING_METHOD = "generate__To__String";

    void addPart(TypeSpec.Builder classBuilder, Generator generator);
}
