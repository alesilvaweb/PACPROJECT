package com.aapm.app.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.aapm.app.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.aapm.app.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.aapm.app.domain.User.class.getName());
            createCache(cm, com.aapm.app.domain.Authority.class.getName());
            createCache(cm, com.aapm.app.domain.User.class.getName() + ".authorities");
            createCache(cm, com.aapm.app.domain.Local.class.getName());
            createCache(cm, com.aapm.app.domain.Local.class.getName() + ".reservas");
            createCache(cm, com.aapm.app.domain.Arquivo.class.getName());
            createCache(cm, com.aapm.app.domain.Associado.class.getName());
            createCache(cm, com.aapm.app.domain.Associado.class.getName() + ".reservas");
            createCache(cm, com.aapm.app.domain.Associado.class.getName() + ".contatos");
            createCache(cm, com.aapm.app.domain.Associado.class.getName() + ".dependentes");
            createCache(cm, com.aapm.app.domain.Dependente.class.getName());
            createCache(cm, com.aapm.app.domain.Contato.class.getName());
            createCache(cm, com.aapm.app.domain.Convenio.class.getName());
            createCache(cm, com.aapm.app.domain.Convenio.class.getName() + ".imagens");
            createCache(cm, com.aapm.app.domain.Convenio.class.getName() + ".redesSociais");
            createCache(cm, com.aapm.app.domain.ImagensConvenio.class.getName());
            createCache(cm, com.aapm.app.domain.RedesSociaisConvenio.class.getName());
            createCache(cm, com.aapm.app.domain.IconsRedesSociais.class.getName());
            createCache(cm, com.aapm.app.domain.IconsRedesSociais.class.getName() + ".redeSocials");
            createCache(cm, com.aapm.app.domain.Reserva.class.getName());
            createCache(cm, com.aapm.app.domain.Departamento.class.getName());
            createCache(cm, com.aapm.app.domain.Departamento.class.getName() + ".reservas");
            createCache(cm, com.aapm.app.domain.Parametro.class.getName());
            createCache(cm, com.aapm.app.domain.Mensagem.class.getName());
            createCache(cm, com.aapm.app.domain.Categoria.class.getName());
            createCache(cm, com.aapm.app.domain.Categoria.class.getName() + ".convenios");
            createCache(cm, com.aapm.app.domain.Tipo.class.getName());
            createCache(cm, com.aapm.app.domain.Tipo.class.getName() + ".mensagems");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
