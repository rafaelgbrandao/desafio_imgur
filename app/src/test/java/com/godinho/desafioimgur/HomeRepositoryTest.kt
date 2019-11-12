package com.godinho.desafioimgur

import com.godinho.desafioimgur.feature.home.repository.*
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class HomeRepositoryTest {

    @MockK
    private lateinit var homeRemoteSource: HomeRemoteSource

    private val homeRepository by lazy {
        spyk(
            HomeRepositoryImpl(homeRemoteSource)
        )
    }

    private val emptyImageResponse = ResponseObject(
        listOf(
            GalleryResponse(
                listOf(
                    ImageResponse()
                )
            )
        )
    )

    private val emptyGalleryResponse = ResponseObject(
        listOf(
            GalleryResponse(emptyList())
        )
    )

    private val emptyContentResponse = ResponseObject(emptyList())


    private val contentResponse = ResponseObject(
        listOf(
            GalleryResponse(
                listOf(
                    ImageResponse(
                        link = LINK,
                        title = "My cat",
                        type = VIDEO_MIME_TYPE
                    ),
                    ImageResponse(
                        link = LINK,
                        title = "My cat 2",
                        type = HomeRepositoryImpl.IMG_MINE_TYPE
                    )
                )
            )
        )
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `when request content list returns response with no content gallery`() {
        every {
            homeRemoteSource.getCatListAsync()
        } returns CompletableDeferred(emptyContentResponse)

        runBlocking {
            val contentList = homeRepository.getCatList()
            Assert.assertEquals(emptyList<String>(), contentList)
        }
    }

    @Test
    fun `when request content list returns response with content but gallery is empty`() {
        every {
            homeRemoteSource.getCatListAsync()
        } returns CompletableDeferred(emptyGalleryResponse)

        runBlocking {
            val contentList = homeRepository.getCatList()
            Assert.assertEquals(emptyList<String>(), contentList)
        }
    }

    @Test
    fun `when request content list returns response with content gallery but images are empty`() {
        every {
            homeRemoteSource.getCatListAsync()
        } returns CompletableDeferred(emptyImageResponse)

        runBlocking {
            val contentList = homeRepository.getCatList()
            Assert.assertEquals(emptyList<String>(), contentList)
        }
    }

    @Test
    fun `when request content list throws exception`() {
        every {
            homeRemoteSource.getCatListAsync()
        } throws Exception()

        runBlocking {
            val contentList = homeRepository.getCatList()
            Assert.assertEquals(null, contentList)
        }
    }

    @Test
    fun `when request content return a list filtered by image type`() {
        every {
            homeRemoteSource.getCatListAsync()
        } returns CompletableDeferred(contentResponse)

        runBlocking {
            val contentList = homeRepository.getCatList()

            Assert.assertEquals(true, contentList?.size == 1)
            Assert.assertEquals(LINK, contentList?.get(0))
        }
    }
    companion object {
        private const val VIDEO_MIME_TYPE = "video/mp4"
        private const val LINK = "https://www.google.com/"
    }
}