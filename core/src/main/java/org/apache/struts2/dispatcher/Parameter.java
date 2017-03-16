package org.apache.struts2.dispatcher;

import java.util.Objects;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface Parameter {

    String getName();

    String getValue();

    boolean isDefined();

    boolean isMultiple();

    String[] getMultipleValues();

    Object getObject();

    class Request implements Parameter {

        private static final Logger LOG = LogManager.getLogger(Request.class);

        private final String name;
        private final Object value;

        public Request(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getValue() {
            String[] values = toStringArray();
            return (values != null && values.length > 0) ? values[0] : null;
        }

        private String[] toStringArray() {
            if (value == null) {
                LOG.trace("The value is null, empty array of string will be returned!");
                return new String[]{};
            } else if (value.getClass().isArray()) {
                LOG.trace("Converting value {} to array of strings", value);

                Object[] values = (Object[]) value;
                String[] strValues = new String[values.length];
                int i = 0;
                for (Object v : values) {
                    strValues[i] = Objects.toString(v, null);
                    i++;
                }
                return strValues;
            } else {
                LOG.trace("Converting value {} to simple string", value);
                return new String[]{ value.toString() };
            }
        }

        @Override
        public boolean isDefined() {
            return value != null && toStringArray().length > 0;
        }

        @Override
        public boolean isMultiple() {
            return isDefined() && toStringArray().length > 1;
        }

        @Override
        public String[] getMultipleValues() {
            return toStringArray();
        }

        @Override
        public Object getObject() {
            return value;
        }

        @Override
        public String toString() {
            return StringEscapeUtils.escapeHtml4(getValue());
        }
    }

    class File extends Request {

        public File(String name, Object value) {
            super(name, value);
        }

        @Override
        public String toString() {
            return "File{" +
                    "name='" + getName() + '\'' +
                    '}';
        }
    }

    class Empty implements Parameter {

        private String name;

        public Empty(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getValue() {
            return null;
        }

        @Override
        public boolean isDefined() {
            return false;
        }

        @Override
        public boolean isMultiple() {
            return false;
        }

        @Override
        public String[] getMultipleValues() {
            return new String[0];
        }

        @Override
        public Object getObject() {
            return null;
        }

        @Override
        public String toString() {
            return "Empty{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

}
