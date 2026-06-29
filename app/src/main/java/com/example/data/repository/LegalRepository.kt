package com.example.data.repository

object LegalRepository {
    fun getEulaText(): String {
        return """
            ITSYOU Fitness – End User License Agreement (EULA)

            Effective Date: 2026

            Welcome to ITSYOU Fitness.

            By downloading, installing, or using this application, you agree to the following terms.

            1. License
            ITSYOU Fitness grants you a limited, non-exclusive, non-transferable license to use this application for your personal, non-commercial fitness purposes.

            2. Ownership
            The application, its original source code, branding, logo, user interface, and original content are owned by ITSYOU Fitness and are protected by applicable intellectual property laws.

            3. Restrictions
            You may not:
            * Copy or redistribute the application.
            * Reverse engineer or modify the application.
            * Sell, rent, or sublicense the application.
            * Remove copyright notices.

            4. Exercise & Health Disclaimer
            Workout programs and fitness information are provided for educational purposes only.
            Consult a qualified healthcare professional before beginning any exercise program.
            You are responsible for your own health and safety while using this application.

            5. No Warranty
            The application is provided "AS IS" without warranties of any kind.

            6. Limitation of Liability
            ITSYOU Fitness shall not be liable for injuries, losses, damages, or claims arising from the use of this application.

            7. Updates
            The application may receive updates, bug fixes, new exercises, AI improvements, and additional features at any time.

            8. Termination
            Violation of this agreement may result in termination of your license to use the application.

            9. Contact
            Support Email: support@itsyoufitness.in
            Website: https://itsyoufitness.in

            10. Copyright
            Copyright © 2026 ITSYOU Fitness. All Rights Reserved.
        """.trimIndent()
    }

    fun getPrivacyPolicyText(): String {
        return """
            ITSYOU Fitness – Privacy Policy

            Effective Date: 2026

            At ITSYOU Fitness, accessible from https://itsyoufitness.in, one of our main priorities is the privacy of our users. This Privacy Policy document contains types of information that is collected and recorded by ITSYOU Fitness and how we use it.

            1. Information We Collect
            We may collect personal information that you provide directly to us, including:
            * Profile Information (e.g., age, height, weight, gender, fitness goals) to calculate precise health metrics.
            * Fitness Activity Logs (e.g., workouts, steps, water intake, stretches).
            * Device Information (e.g., OS version, unique device identifiers) for stability and license verification.

            2. How We Use Your Information
            We use the collected information to:
            * Provide, operate, and maintain our fitness tracking services.
            * Personalize and expand our application's kinesiology recommendations.
            * Understand and analyze how you use our application to improve user experience.
            * Protect the integrity of the application and verify official Google Play licenses.

            3. Local Data Storage & Security
            All of your personal and fitness tracking data is stored locally on your device using encrypted SQLite/Room database structures. We do not transmit or sell your health data to third-party brokers.

            4. Google Play Services & Play Integrity
            This app utilizes Google Play Services to verify license status and ensure application safety. Google may collect diagnostic and usage data in accordance with their privacy policies.

            5. Contact Us
            If you have additional questions or require more information about our Privacy Policy, do not hesitate to contact us at support@itsyoufitness.in.
        """.trimIndent()
    }

    fun getTermsOfServiceText(): String {
        return """
            ITSYOU Fitness – Terms of Service

            Effective Date: 2026

            Welcome to ITSYOU Fitness! These Terms of Service govern your use of the ITSYOU Fitness mobile application.

            1. Acceptance of Terms
            By accessing and using this application, you accept and agree to be bound by the terms and provision of this agreement.

            2. Use License
            Permission is granted to download one copy of the application per device for personal, non-commercial transitory viewing and use only.

            3. User Responsibility
            You agree to use this application responsibly. You must not use the application for any illegal purposes, or to transmit malicious code or engage in unauthorized modifications.

            4. Health and Fitness Advice
            The application provides physical exercises, stretching routines, and fitness estimations. This is not medical advice. You agree that your participation in any exercise is entirely voluntary and at your own risk.

            5. Modifications and Updates
            We reserve the right to modify, suspend, or discontinue any aspect of the application at any time, including the availability of features, databases, or content.

            6. Contact
            For inquiries regarding these Terms of Service, please contact support@itsyoufitness.in.
        """.trimIndent()
    }

    fun getDisclaimerText(): String {
        return """
            ITSYOU Fitness – Health & Exercise Disclaimer

            PLEASE READ THIS DISCLAIMER CAREFULLY BEFORE USING THIS APPLICATION.

            1. Not Medical Advice
            The content, exercises, instructions, and calculations provided within ITSYOU Fitness are for informational and educational purposes only. They do not constitute professional medical advice, diagnosis, or treatment.

            2. Consult Your Doctor
            You should consult with a physician or other healthcare professional before starting this or any other fitness program to determine if it is right for your unique physical needs. Do not use this app if your doctor advises against it.

            3. Assumption of Risk
            By using this application, you acknowledge that physical exercise carries inherent risks of injury. You voluntarily assume all risks associated with performing any workout, stretch, or routine shown in this application.

            4. Limitation of Liability
            ITSYOU Fitness, its creators, developers, and partners shall not be held liable for any physical injury, health complication, cognitive distress, or material damage resulting from the use or misuse of the instructions or features provided.
        """.trimIndent()
    }

    fun getCopyrightText(): String {
        return """
            Copyright & Intellectual Property Notice

            © 2026 ITSYOU Fitness. All Rights Reserved.

            1. Proprietary Protection
            The ITSYOU Fitness software application, including all custom user interfaces, layouts, branding assets, logos, compiled source code, visual vector maps, and dynamic muscle-activation algorithms, is the exclusive intellectual property of ITSYOU Fitness.

            2. Trademark Notice
            "ITSYOU", "ITSYOU Fitness", and associated brand assets are registered or protected trademarks of ITSYOU Fitness. Unauthorized use of these marks is strictly prohibited.

            3. Third-Party Licenses
            This application respects open-source standards and integrates components licensed under the Apache License 2.0 and MIT License. Full attribution and compliance with copyright terms are maintained for all utilized libraries.
        """.trimIndent()
    }

    fun getAboutText(): String {
        return """
            About ITSYOU Fitness

            ITSYOU Fitness is a state-of-the-art mobile training companion designed to help you analyze, plan, and optimize your workouts with unmatched anatomical precision.

            Features:
            • Dynamic Muscle-Activation Mapping
            • Personalized Fitness Analytics & Body Metrics
            • Premium Stretching & Exercise Libraries
            • Offline-First Secure local data architecture
            • Fully integrated local sound engine

            Built for dedicated athletes who demand control over their fitness journey.
        """.trimIndent()
    }

    fun getContactText(): String {
        return """
            Contact Support & Legal

            We are here to assist you with any inquiries regarding technical support, subscription questions, or legal compliance.

            Official Support Email:
            support@itsyoufitness.in

            Official Website:
            https://itsyoufitness.in

            Developer & Publisher:
            ITSYOU Fitness Ltd.
        """.trimIndent()
    }
}
