# 🎮 Clash of Words

[![Android](https://img.shields.io/badge/Android-API%2028+-green.svg)](https://developer.android.com/about/versions/android-9.0)
[![Java](https://img.shields.io/badge/Java-11-orange.svg)](https://www.oracle.com/java/)
[![Firebase](https://img.shields.io/badge/Firebase-9.0+-yellow.svg)](https://firebase.google.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Clash of Words, Türkçe kelime tabanlı çok oyunculu bir Android oyunudur. Oyuncular gerçek zamanlı olarak kelime sorularını yanıtlayarak puan kazanır ve arkadaşlarıyla rekabet eder.

## ✨ Özellikler

- 🎯 **Çok Oyunculu Oyun**: Arkadaşlarınızla veya botlarla gerçek zamanlı kelime savaşları
- 👤 **Kullanıcı Profilleri**: Özel avatarlar, ilerleme takibi ve liderlik tablosu
- 🛒 **Oyun İçi Mağaza**: Güçlendiriciler, avatar geliştirmeleri ve ek oyun içeriği satın alma
- 🎨 **Modern Arayüz**: Sezgisel navigasyon, animasyonlu geçişler ve duyarlı tasarım
- 🔥 **Firebase Entegrasyonu**: Güvenli kullanıcı kimlik doğrulama ve gerçek zamanlı veri senkronizasyonu
- 📊 **İstatistikler**: Oyun performansınızı takip edin ve geliştirin

## 🛠️ Kullanılan Teknolojiler

- **Java 11**: Ana programlama dili
- **Android SDK**: Mobil uygulama geliştirme
- **Firebase**: 
  - Authentication (Kimlik doğrulama)
  - Firestore (Veritabanı)
  - Storage (Dosya depolama)
  - Crashlytics (Hata takibi)
- **Android Architecture Components**: ViewModel, LiveData, Navigation
- **Material Design**: Modern ve duyarlı UI bileşenleri
- **Glide**: Görsel yükleme ve önbellekleme

## 📱 Ekran Görüntüleri

<!-- Buraya ekran görüntüleri eklenebilir -->

## 🚀 Kurulum

### Gereksinimler
- Android Studio Arctic Fox veya üzeri
- Android SDK API 28+
- Java 11
- Firebase hesabı

### Adımlar

1. **Projeyi klonlayın:**
   ```bash
   git clone https://github.com/furblood0/clash-of-words.git
   cd clash-of-words
   ```

2. **Firebase projesini oluşturun:**
   - [Firebase Console](https://console.firebase.google.com/)'a gidin
   - Yeni proje oluşturun
   - Android uygulaması ekleyin (`com.furkan.clashofwords`)
   - `google-services.json` dosyasını indirin

3. **Firebase yapılandırması:**
   - İndirilen `google-services.json` dosyasını `app/` klasörüne yerleştirin
   - Firebase Console'da Authentication, Firestore ve Storage servislerini etkinleştirin

4. **Projeyi açın:**
   - Android Studio'da projeyi açın
   - Gradle senkronizasyonunu bekleyin
   - Emülatör veya fiziksel cihazda çalıştırın

## 📁 Proje Yapısı

```
app/src/main/java/com/furkan/clashofwords/
├── Activities/
│   ├── MainActivity.java          # Uygulama giriş noktası
│   └── HomeActivity.java          # Ana hub aktivitesi
├── ui/
│   ├── gameplay/                  # Oyun mekanikleri
│   ├── home/                      # Ana sayfa
│   ├── profile/                   # Kullanıcı profili
│   ├── shop/                      # Oyun içi mağaza
│   ├── friends/                   # Arkadaş listesi
│   └── settings/                  # Ayarlar
├── Adapters/                      # RecyclerView adaptörleri
└── Helpers/                       # Yardımcı sınıflar
```

## 🎮 Oyun Özellikleri

### Soru Kategorileri
- 🍽️ **Gıda ve Yemek**: Türk mutfağı, kahvaltı, çorbalar
- 🔬 **Teknoloji ve Bilim**: Programlama, uzay, enerji
- ⚽ **Spor ve Fitness**: Futbol, olimpiyatlar, sporcular
- 🎭 **Eğlence ve Kültür**: Film, müzik, sanat
- 🌍 **Genel Kültür**: Tarih, coğrafya, güncel olaylar

### Oyun Mekanikleri
- ⏱️ **Zaman Sınırı**: Her soru için belirli süre
- 💰 **Altın Sistemi**: Doğru cevaplarla altın kazanma
- ⚡ **Enerji Sistemi**: Oyun oynamak için enerji tüketimi
- 🏆 **Liderlik Tablosu**: Arkadaşlar arası sıralama

## 🔧 Geliştirme

### Katkıda Bulunma

1. Bu repository'yi fork edin
2. Yeni bir branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Değişikliklerinizi commit edin (`git commit -m 'Add amazing feature'`)
4. Branch'inizi push edin (`git push origin feature/amazing-feature`)
5. Pull Request oluşturun

### Kod Stili

- Java naming conventions'ı takip edin
- Türkçe yorumlar kullanın
- Material Design guidelines'ı uygulayın
- Firebase best practices'ini takip edin

## 📄 Lisans

Bu proje MIT lisansı altında lisanslanmıştır. Detaylar için [LICENSE](LICENSE) dosyasına bakın.

## 👨‍💻 Geliştirici

**Furkan** - [GitHub](https://github.com/furblood0)

## 🙏 Teşekkürler

- Firebase ekibine harika backend servisleri için
- Android geliştirici topluluğuna açık kaynak kütüphaneler için
- Test eden ve geri bildirim veren tüm kullanıcılara

---

⭐ Bu projeyi beğendiyseniz yıldız vermeyi unutmayın!
