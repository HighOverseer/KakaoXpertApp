package com.neotelemetrixgdscunand.kakaoxpert.domain.model

data class SupplierCacao(
    val id: Int,
    val imageUrl: String,
    val name: String
)

fun getSupplierCacaoDummies(): List<SupplierCacao> {
    return List(3) {
        SupplierCacao(
            id = it,
            "https://s3-alpha-sig.figma.com/img/7bb8/e06d/7ba6df889cfe83fee1003b145be98bb3?Expires=1737936000&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=oO74RgJwXOdzxaU9NVzA4Mt0Rt3VsxbvX2PAkkl7qPo2Sx8-8trQ4ARbZ4LcfBd9SLHVG9jYnyj6~RVY~QAGjUmE4yjExH5GSdZkpe6J8tPPRaRh7o1lGEApTmw9WX7Vh82yz0qioLfk4WKWpKvVEqnHrRM-U-sOCoGe-Vw41Y4~XrOuugCNmJ4DXAB1N7bn8ds1FWg1m1u5PAWZ2-UGc~mJLtBcCzEKZ~oaM35D9xSLRNhRgYx6oiLt~sjdmM0sbi6RqKqRNP-HyumNuRyeYJNp5PH0y6K7WCgdGfH6NghnDapXYtU22eoY9WNbPD6JtbmhZUYl8DZkhEieYb0CvA__",
            "John Doe"
        )
    }
}