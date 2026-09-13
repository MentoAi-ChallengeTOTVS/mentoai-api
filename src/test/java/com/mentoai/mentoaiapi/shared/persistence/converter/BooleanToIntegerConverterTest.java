package com.mentoai.mentoaiapi.shared.persistence.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class BooleanToIntegerConverterTest {
    private final BooleanToIntegerConverter converter = new BooleanToIntegerConverter();

    @Test
    void preservaNullParaFiltrosOpcionais() {
        assertNull(converter.convertToDatabaseColumn(null));
        assertNull(converter.convertToEntityAttribute(null));
    }

    @Test
    void mantemConversaoDeStatusAtivoEInativo() {
        assertEquals(1, converter.convertToDatabaseColumn(true));
        assertEquals(0, converter.convertToDatabaseColumn(false));
        assertEquals(true, converter.convertToEntityAttribute(1));
        assertEquals(false, converter.convertToEntityAttribute(0));
    }
}
