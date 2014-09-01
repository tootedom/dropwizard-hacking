package org.greencheek.dropwtutorial.configuration;

import com.yammer.tenacity.core.properties.TenacityPropertyKey;
import com.yammer.tenacity.core.properties.TenacityPropertyKeyFactory;
import org.greencheek.dropwtutorial.clientconfig.ClientLibPropertyKeys;

/**
 * Created by dominictootell on 31/08/2014.
 */
public class ClientPropertyKeyFactory implements TenacityPropertyKeyFactory {
    @Override
    public TenacityPropertyKey from(String s) {
        try {
            return ClientPropertyKeys.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ClientLibPropertyKeys.valueOf(s.toUpperCase());
        }
    }
}
