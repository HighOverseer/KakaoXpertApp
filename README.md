<img width="1618" height="381" alt="Group 32-4" src="https://github.com/user-attachments/assets/46c25528-7818-46c9-ac04-40b326a853d0" />


# KakaoXpert App

An AI-powered mobile application designed to help cocoa farmers detect diseases, assess damage levels, and manage the health of their cocoa plants through advanced computer vision and IoT integration.

![98f92e8819a7b04c88cee1e8863597fa_upscaled](https://github.com/user-attachments/assets/635f3dda-6567-4fa4-b895-a5c751a2fc0f)

## 🌱 Overview

KakaoXpert leverages artificial intelligence to analyze cocoa pod images, identify diseases, and predict damage extent. The app integrates with IoT devices to collect environmental data, providing farmers with comprehensive insights and treatment recommendations to optimize their cocoa crop management.

## ✨ Key Features

### 🔍 Disease Detection & Analysis
- **Automatic Disease Detection**: Uses locally integrated YOLOv8 model to identify diseases from camera photos or gallery images
- **Damage Assessment**: YOLOv8-seg model performs instance segmentation to calculate precise damage percentages
- **Price Prediction**: Estimates selling prices based on damage levels and daily updated market reference prices

### 🌡️ IoT Integration
- **Environmental Monitoring**: Displays real-time data from connected sensors (temperature, light intensity, soil moisture)
- **Data Visualization**: Presents sensor data in interactive graph formats
- **Device Management**: Allows users to manage and configure connected IoT devices

### 💡 Smart Recommendations
- **Customized Treatment Solutions**: Provides tailored recommendations using Qwen+ LLM based on disease type and environmental conditions
- **Data-Driven Insights**: Combines AI analysis with environmental data for accurate treatment suggestions

### 📊 Data Management
- **Analysis History**: Comprehensive record of all analysis sessions for tracking and review
- **Offline Functionality**: Core analysis features work without internet connection thanks to embedded AI models
- **Data Synchronization**: Seamless sync when connection is restored

### 🏠 User Experience
- **Dashboard**: Real-time weather information and IoT sensor overview
- **Agricultural News**: Latest industry updates and farming insights
- **User Management**: Complete account system with login, registration, and profile management

## 🛠️ Technology Stack

### Mobile Development
- **Framework**: Android with Jetpack Compose
- **Architecture**: Clean Architecture + MVVM pattern
- **Pagination**: Paging3 for efficient data loading

### AI & Machine Learning
- **Object Detection**: YOLOv8 for disease identification
- **Segmentation**: YOLOv8-seg for damage level calculation
- **Language Model**: Qwen+ LLM for treatment recommendations
- **ML Framework**: TensorFlow Lite for local model inference

### Data & Networking
- **API Communication**: Retrofit for network requests
- **Local Database**: Room Database for offline data storage
- **Preferences**: DataStore for application settings
- **Background Processing**: WorkManager for offline data sync

### Camera & Media
- **Camera Integration**: CameraX for custom camera functionality
- **Image Processing**: Advanced image analysis capabilities

### Architecture & DI
- **Dependency Injection**: Dagger Hilt for modular architecture
- **Clean Architecture**: Separation of concerns with clear layer boundaries

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 21+
- Internet connection for initial setup and IoT features

### Installation
1. Clone the repository
```bash
git clone https://github.com/yourusername/KakaoXpertApp.git
```

2. Open the project in Android Studio

3. Sync project with Gradle files

4. Build and run on your Android device or emulator

## 📱 Usage

1. **Setup Account**: Register or login to access full features
2. **Connect IoT Devices**: Pair your environmental sensors for real-time monitoring
3. **Analyze Cocoa Pods**: Take photos of cocoa pods to detect diseases and assess damage
4. **Review Results**: Check damage levels, price predictions, and treatment recommendations
5. **Monitor Environment**: Track environmental conditions through the IoT dashboard
6. **Access History**: Review past analyses and track crop health trends

## 🌐 Offline Capabilities

KakaoXpert is designed to work in areas with limited internet connectivity:
- Disease detection and damage assessment work completely offline
- Analysis history is stored locally and synced when connection is available
- Core AI models are embedded within the app for reliable performance

## 🤖 App Demo Video
Link : [KakaoXpert App Demo](https://drive.google.com/file/d/1bdo31vowj9H1WWXwKxdU5OtXh2gnzrsT/view?usp=sharing)

## 🤝 Contributing

We welcome contributions to improve KakaoXpert! Please feel free to:
- Report bugs and issues
- Suggest new features
- Submit pull requests
- Improve documentation

## 📞 Support

For support, questions, or feedback:
- Create an issue in this repository
- Contact the development team
- Check the documentation wiki

## 🙏 Acknowledgments

- Thanks to the cocoa farming community for their valuable feedback
- All contributors who helped make this project possible

---

**KakaoXpert** - Empowering cocoa farmers through AI and IoT technology 🍫🌱
