package org.greencheek.dropwtutorial.clientconfig;

import com.yammer.tenacity.core.properties.TenacityPropertyKey;
import com.yammer.tenacity.core.properties.TenacityPropertyKeyFactory;
import org.greencheek.dropwtutorial.configuration.ClientPropertyKeys;

/**
 * Created by dominictootell on 31/08/2014.
 */
public class ClientLibPropertyKeyFactory implements TenacityPropertyKeyFactory {

    private TenacityPropertyKeyFactory delegate;

    public ClientLibPropertyKeyFactory(TenacityPropertyKeyFactory delegate)
    {
        this.delegate = delegate;
    }

    @Override
    public TenacityPropertyKey from(String s) {
        try {
            return ClientLibPropertyKeys.valueOf(s.toUpperCase());
        } catch(Exception e) {
            return delegate.from(s);
        }
    }
}
