package com.example.twocreate.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

final class Mapper<T> {

    final Logger logger = LoggerFactory.getLogger(getClass());

    final Class<T> entityClass;

    final Constructor<T> constructor;

    final String tableName;

    //@Id property:
    final AccessibleProperty id;

    //all properties including @Id, key is property name
    final List<AccessibleProperty> allProperties;

    // property name -> AccessibleProperty
    final Map<String, AccessibleProperty> allPropertiesMap;

    final List<AccessibleProperty> insertableProperties;

    final List<AccessibleProperty> updatableProperties;

    // property name -> AccessibleProperty
    final Map<String, AccessibleProperty> updatablePropertiesMap;

    final ResultSetExtractor<List<T>> resultSetExtractor;

    final String selectSQL;
    final String insertSQL;
    final String insertIgnoreSQL;
    final String updateSQL;
    final String deleteSQL;

    public T newInstance() throws ReflectiveOperationException {
        return this.constructor.newInstance();
    }

    public Mapper(Class<T> clazz) throws Exception {
        List<AccessibleProperty> all = getProperties(clazz);
        AccessibleProperty[] ids = all.stream().filter(AccessibleProperty::isId).toArray(AccessibleProperty[]::new);
        if (ids.length != 1) {
            throw new RuntimeException("Require exact one @Id for class " + clazz.getName());
        }
        this.id = ids[0];
        this.allProperties = all;
        this.allPropertiesMap = buildPropertiesMap(this.allProperties);
        this.insertableProperties = all.stream().filter(AccessibleProperty::isInsertable).collect(Collectors.toList());
        this.updatableProperties = all.stream().filter(AccessibleProperty::isUpdatable).collect(Collectors.toList());
        this.updatablePropertiesMap = buildPropertiesMap(this.updatableProperties);
        this.entityClass = clazz;
        this.constructor = clazz.getConstructor();
        this.tableName = getTableName(clazz);
        this.selectSQL = "SELECT * FROM " + this.tableName + " WHERE " + this.id.propertyName + " = ?";
        this.insertSQL = "INSERT INTO " + this.tableName + " ("
                + String.join(", ",this.insertableProperties.stream().map(p -> p.propertyName).toArray(String[]::new))
                + ") VALUES (" + numOfQuestions(this.insertableProperties.size()) + ")";
        this.insertIgnoreSQL = this.insertSQL.replace("INSERT INTO","INSERT IGNORE INTO");
    }

    Map<String, AccessibleProperty> buildPropertiesMap(List<AccessibleProperty> props){
        Map<String , AccessibleProperty> map = new HashMap<>();
        for (AccessibleProperty prop: props) {
            map.put(prop.propertyName, prop);
        }
        return map;
    }

    private String numOfQuestions(int n) {
        String[] qs = new String[n];
        return String.join(", ", Arrays.stream(qs).map((s) -> {
            return "?";
        }).toArray(String[]::new));
    }
    private String getTableName(Class<?> clazz){
        Table table = clazz.getAnnotation(Table.class);
        if (table != null && !table.name().isEmpty()) {
            return table.name();
        }
        String name = clazz.getSimpleName();
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }
    private List<AccessibleProperty> getProperties(Class<?> clazz) throws Exception {
        List<AccessibleProperty> properties = new ArrayList<>();
        for (Field f : clazz.getFields()) {
            if (Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            if (f.isAnnotationPresent(Transient.class)) {
                continue;
            }
            var p = new AccessibleProperty(f);
            logger.debug("found accessible property: {}", p);
            properties.add(p);
        }
        return properties;
    }
}
