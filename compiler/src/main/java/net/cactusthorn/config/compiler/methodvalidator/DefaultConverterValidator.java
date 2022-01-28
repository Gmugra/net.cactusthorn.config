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
package net.cactusthorn.config.compiler.methodvalidator;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import net.cactusthorn.config.compiler.ProcessorException;
import net.cactusthorn.config.core.converter.Converter;
import net.cactusthorn.config.core.converter.bytesize.ByteSize;
import net.cactusthorn.config.core.converter.standard.ByteSizeConverter;
import net.cactusthorn.config.core.converter.standard.CharacterConverter;
import net.cactusthorn.config.core.converter.standard.CurrencyConverter;
import net.cactusthorn.config.core.converter.standard.DurationConverter;
import net.cactusthorn.config.core.converter.standard.InstantConverter;
import net.cactusthorn.config.core.converter.standard.LocalDateConverter;
import net.cactusthorn.config.core.converter.standard.LocalDateTimeConverter;
import net.cactusthorn.config.core.converter.standard.LocalTimeConverter;
import net.cactusthorn.config.core.converter.standard.LocaleConverter;
import net.cactusthorn.config.core.converter.standard.OffsetDateTimeConverter;
import net.cactusthorn.config.core.converter.standard.OffsetTimeConverter;
import net.cactusthorn.config.core.converter.standard.PathConverter;
import net.cactusthorn.config.core.converter.standard.PeriodConverter;
import net.cactusthorn.config.core.converter.standard.URIConverter;
import net.cactusthorn.config.core.converter.standard.URLConverter;
import net.cactusthorn.config.core.converter.standard.YearConverter;
import net.cactusthorn.config.core.converter.standard.YearMonthConverter;
import net.cactusthorn.config.core.converter.standard.ZonedDateTimeConverter;

public class DefaultConverterValidator extends MethodValidatorAncestor {

    public static final Map<Class<?>, String> CONVERTERS;
    static {
        CONVERTERS = new HashMap<>();
        CONVERTERS.put(URL.class, URLConverter.class.getName());
        CONVERTERS.put(URI.class, URIConverter.class.getName());
        CONVERTERS.put(Path.class, PathConverter.class.getName());
        CONVERTERS.put(Currency.class, CurrencyConverter.class.getName());
        CONVERTERS.put(Locale.class, LocaleConverter.class.getName());
        CONVERTERS.put(ByteSize.class, ByteSizeConverter.class.getName());
        CONVERTERS.put(Character.class, CharacterConverter.class.getName());
        CONVERTERS.put(Instant.class, InstantConverter.class.getName());
        CONVERTERS.put(Duration.class, DurationConverter.class.getName());
        CONVERTERS.put(Period.class, PeriodConverter.class.getName());
        CONVERTERS.put(LocalDate.class, LocalDateConverter.class.getName());
        CONVERTERS.put(LocalDateTime.class, LocalDateTimeConverter.class.getName());
        CONVERTERS.put(LocalTime.class, LocalTimeConverter.class.getName());
        CONVERTERS.put(ZonedDateTime.class, ZonedDateTimeConverter.class.getName());
        CONVERTERS.put(OffsetDateTime.class, OffsetDateTimeConverter.class.getName());
        CONVERTERS.put(OffsetTime.class, OffsetTimeConverter.class.getName());
        CONVERTERS.put(Year.class, YearConverter.class.getName());
        CONVERTERS.put(YearMonth.class, YearMonthConverter.class.getName());
    }

    private final Map<TypeMirror, Type> classTypes = new HashMap<>();

    public DefaultConverterValidator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
        for (Class<?> clazz : CONVERTERS.keySet()) {
            classTypes.put(processingEnv().getElementUtils().getTypeElement(clazz.getName()).asType(), clazz);
        }
    }

    @Override public MethodInfo validate(ExecutableElement methodElement, TypeMirror typeMirror) throws ProcessorException {
        if (typeMirror.getKind() != TypeKind.DECLARED) {
            return next(methodElement, typeMirror);
        }
        DeclaredType declaredType = (DeclaredType) typeMirror;
        Element element = declaredType.asElement();
        if (existConverterAnnotation(methodElement)) {
            return next(methodElement, typeMirror);
        }
        // @formatter:off
        Optional<Type> classType =
            classTypes.entrySet().stream()
            .filter(e -> processingEnv().getTypeUtils().isSameType(element.asType(), e.getKey()))
            .map(e -> e.getValue())
            .findAny();
        // @formatter:off
        if (!classType.isPresent()) {
            return next(methodElement, typeMirror);
        }
        TypeMirror converter = processingEnv().getElementUtils().getTypeElement(CONVERTERS.get(classType.get())).asType();
        return new MethodInfo(methodElement).withConverter(converter, Converter.EMPTY);
    }

    protected boolean existsConvertorAnotation(ExecutableElement methodElement) {
        return existConverterAnnotation(methodElement);
    }
}
