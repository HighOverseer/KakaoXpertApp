package com.neotelemetrixgdscunand.kakaoxpert.domain.model

import kotlin.random.Random

data class ShopItemData(
    val id: Int = Random.nextInt(0, 1_000_000),
    val imageUrl: String,
    val title: String,
    val price: String,
    val targetUrl: String
)


fun getShopItemDataDummies(): List<ShopItemData> {
    return listOf(
        ShopItemData(
            imageUrl = "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/2/28/8b27f067-59f5-44e9-93b1-0236408355f5.jpg",
            title = "Pupuk Organik Booster Kakao Cokelat Cepat Berbuah Lebat Melimpah",
            price = "Rp18.750",
            targetUrl = "https://www.tokopedia.com/centralorganik/pupuk-organik-booster-kakao-cokelat-cepat-berbuah-lebat-melimpah?extParam=cmp%3D1%26ivf%3Dfalse&src=topads"
        ),
        ShopItemData(
            imageUrl = "https://images.tokopedia.net/img/cache/900/VqbcmM/2024/5/12/ed944964-ce87-439d-abd0-455f5f93a332.png",
            title = "Pupuk Kakao Coklat / POC Booster Kakao / Pupuk Organik Cair Booster Pelebat Buah Kakao",
            price = "Rp75.000",
            targetUrl = "https://www.tokopedia.com/intigro/pupuk-kakao-coklat-poc-booster-kakao-pupuk-organik-cair-booster-pelebat-buah-kakao-penyubur-daun-akar-batang-penguat-akar-penumbuh-buah-pemacu-tumbuh-tanaman-buah-kakao-agar-tidak-rontok-anti-hama-kutu-daun-ulat-penggerek-batang?extParam=ivf%3Dfalse&src=topads"
        ),
        ShopItemData(
            imageUrl = "https://images.tokopedia.net/img/cache/900/hDjmkQ/2024/6/7/c5441be2-5369-46b7-accb-1d0c37c8cddc.jpg",
            title = "Pupuk Organik Cair Buah Kakao / Pupuk Kakao Cepat Berbuah / Pupuk Pelebat Penyubur Kakao / POC Kakao",
            price = "Rp70.000",
            targetUrl = "https://www.tokopedia.com/vasskinid/pupuk-organik-cair-buah-kakao-pupuk-kakao-cepat-berbuah-pupuk-pelebat-penyubur-kakao-poc-kakao?extParam=ivf%3Dfalse%26keyword%3Dpupuk+kakao%26search_id%3D20241115185256D3E509378F987E00BNWL%26src%3Dsearch"
        ),
        ShopItemData(
            imageUrl = "https://images.tokopedia.net/img/cache/900/VqbcmM/2024/10/19/e7c285b4-f4cd-4f15-b901-ebeaa0a47048.jpg",
            title = "Alitura Pupuk Organik Kakao Cacao Nutrisi 100% Organic 500gr 2Kg 20Kg SPECIAL MIX Siap Pakai - 500gr",
            price = "Rp65.000",
            targetUrl = "https://www.tokopedia.com/alituraorganics/alitura-pupuk-organik-kakao-cacao-nutrisi-100-organic-500gr-2kg-20kg-special-mix-siap-pakai-500gr-0eeef?extParam=ivf%3Dfalse%26keyword%3Dpupuk+kakao%26search_id%3D20241115191518F47751F68FA5173480GQ%26src%3Dsearch"
        ),
        ShopItemData(
            imageUrl = "https://images.tokopedia.net/img/cache/900/VqbcmM/2024/5/11/003e8c23-339b-479b-a9d2-6bb3ba2286eb.jpg",
            title = "Pupuk Buah Tanaman Kakao, Pupuk Pelebat Buah Kakao, Pupuk Pembesar Buah Kakao FRUIT EX 250 Gr",
            price = "Rp49.000",
            targetUrl = "https://www.tokopedia.com/el-comers/pupuk-buah-tanaman-kakao-pupuk-pelebat-buah-kakao-pupuk-pembesar-buah-kakao-fruit-ex-250-gr?extParam=ivf%3Dfalse%26keyword%3Dpupuk+kakao%26search_id%3D20241115191707B5E148535D05CE3D9DY4%26src%3Dsearch"
        ),
        ShopItemData(
            imageUrl = "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/4/15/31477f3b-d6fd-47a5-939e-a5a44c6a8bef.jpg",
            title = "Pupuk Nutrisi Tanaman Kakao Coklat Buah, Fortune Anti Rontok Bunga",
            price = "Rp35.000",
            targetUrl = "https://www.tokopedia.com/pekaranganlestari/pupuk-nutrisi-tanaman-kakao-coklat-buah-fortune-anti-rontok-bunga?extParam=ivf%3Dfalse%26keyword%3Dpupuk+kakao%26search_id%3D20241115191830A88038D44782132E028K%26src%3Dsearch"
        ),
        ShopItemData(
            imageUrl = "https://images.tokopedia.net/img/cache/900/VqbcmM/2024/10/19/e7c285b4-f4cd-4f15-b901-ebeaa0a47048.jpg",
            title = "Alitura Pupuk Organik Kakao Cacao Nutrisi 100% Organic 500gr 2Kg 20Kg SPECIAL MIX Siap Pakai - 500gr",
            price = "Rp65.000",
            targetUrl = "https://www.tokopedia.com/alituraorganics/alitura-pupuk-organik-kakao-cacao-nutrisi-100-organic-500gr-2kg-20kg-special-mix-siap-pakai-500gr-0eeef?extParam=ivf%3Dfalse%26keyword%3Dpupuk+kakao%26search_id%3D20241115191518F47751F68FA5173480GQ%26src%3Dsearch"
        ),
        ShopItemData(
            imageUrl = "https://images.tokopedia.net/img/cache/900/VqbcmM/2024/5/11/003e8c23-339b-479b-a9d2-6bb3ba2286eb.jpg",
            title = "Pupuk Buah Tanaman Kakao, Pupuk Pelebat Buah Kakao, Pupuk Pembesar Buah Kakao FRUIT EX 250 Gr",
            price = "Rp49.000",
            targetUrl = "https://www.tokopedia.com/el-comers/pupuk-buah-tanaman-kakao-pupuk-pelebat-buah-kakao-pupuk-pembesar-buah-kakao-fruit-ex-250-gr?extParam=ivf%3Dfalse%26keyword%3Dpupuk+kakao%26search_id%3D20241115191707B5E148535D05CE3D9DY4%26src%3Dsearch"
        ),
        ShopItemData(
            imageUrl = "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/4/15/31477f3b-d6fd-47a5-939e-a5a44c6a8bef.jpg",
            title = "Pupuk Nutrisi Tanaman Kakao Coklat Buah, Fortune Anti Rontok Bunga",
            price = "Rp35.000",
            targetUrl = "https://www.tokopedia.com/pekaranganlestari/pupuk-nutrisi-tanaman-kakao-coklat-buah-fortune-anti-rontok-bunga?extParam=ivf%3Dfalse%26keyword%3Dpupuk+kakao%26search_id%3D20241115191830A88038D44782132E028K%26src%3Dsearch"
        ),
    )
}