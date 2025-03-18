package ru.webshop.backend.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI

@Configuration
class S3ClientConfig(
    @Value("\${s3.bucket}")
    val bucket: String,
    @Value("\${s3.region}")
    final val region: String,
    @Value("\${s3.minioUrl")
    final val minioUrl: String,
    @Value("\${s3.accessKeyId}")
    final val accessKeyId: String,
    @Value("\${s3.secretAccessKey}")
    final val secretAccessKey: String
) {
    final val credentials = AwsBasicCredentials.create(
        accessKeyId,
        secretAccessKey
    )

    val s3Client = S3Client.builder()
        .endpointOverride(URI.create(minioUrl))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .region(Region.of(region))
        .forcePathStyle(true)
        .build()

    @Bean
    fun S3ClientMinio(

    ): S3Client {

    }
}