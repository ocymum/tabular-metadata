
package com.asoroka.sidora.tabularmetadata.datatype;

import java.lang.reflect.Type;

/**
 * A type that possesses some defined map to a Java value space: the space of instances of JavaValueSpace.
 * 
 * @author ajs6f
 * @param <JavaValueSpace>
 */
public interface TypeWithMapToValueSpace extends Type {

    <JavaValueSpace extends Object> JavaValueSpace parse(final String s) throws ParsingException;

}
