package com.madadipouya.elasticsearch.springdata;

import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

public class BookElasticsearchContainer extends ElasticsearchContainer {

    private static final String ELASTIC_SEARCH_DOCKER = "elasticsearch:6.4.3";

    private static final String CLUSTER_NAME = "cluster.name";

    private static final String ELASTIC_SEARCH = "elasticsearch";

    public BookElasticsearchContainer() {
        super(DockerImageName.parse(ELASTIC_SEARCH_DOCKER)
                .asCompatibleSubstituteFor("docker.elastic.co/elasticsearch/elasticsearch"));
        this.addFixedExposedPort(9200, 9200);
        this.addFixedExposedPort(9300, 9300);
        this.addEnv(CLUSTER_NAME, ELASTIC_SEARCH);
    }
}
