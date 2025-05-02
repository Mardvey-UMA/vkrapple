package ru.webshop.backend.repository

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch.core.SearchRequest
import org.springframework.stereotype.Repository
import ru.webshop.backend.document.ProductDocument
import co.elastic.clients.elasticsearch._types.query_dsl.*
import co.elastic.clients.elasticsearch.core.SearchResponse
import co.elastic.clients.elasticsearch.core.search.Hit
import java.io.IOException
import java.util.stream.Collectors
@Repository
class ProductDocumentRepository(
    private val elasticsearchClient: ElasticsearchClient
){

    fun weightedSearch(query: String): List<ProductDocument> {
        val searchRequest = SearchRequest.of { s ->
            s.index("products")
                .query(
                    Query.of { q ->
                        q.multiMatch(
                            MultiMatchQuery.of { m ->
                                m.query(query)
                                    .fields(
                                        "name^3",
                                        "values.value^2",
                                        "description^1"
                                    )
                            }
                        )
                    }
                )
                .from(0)
                .size(10)
        }
        try {
            val response = elasticsearchClient.search(searchRequest, ProductDocument::class.java)
        } catch (e: Exception){
            println(e.stackTrace)
        }
        val response = elasticsearchClient.search(searchRequest, ProductDocument::class.java)
        return response.hits().hits().mapNotNull { it.source() }
    }
}