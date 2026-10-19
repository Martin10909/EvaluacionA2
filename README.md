# LocalCash - Sistema de Gestión Financiera Personal y Grupal Offline

**Asignatura:** Desarrollo de Aplicaciones Móviles  
**Docente:** Benjamín Mora Torres  
**Estudiante:** Proyecto de Evaluación A2  
**Plataforma:** Android Nativo (Java)  

---

## 1. Descripción del Proyecto

**LocalCash** es una aplicación móvil nativa desarrollada en Java para Android, diseñada para operar de forma 100% local y offline. Permite el control de cuentas personales, registro de transacciones georreferenciadas, planificación de compras futuras y división de gastos compartidos entre contactos, garantizando la privacidad de los datos financieros mediante autenticación biométrica y persistencia exclusiva en SQLite.

---

## 2. Los 4 Pilares Obligatorios

1. **Desarrollo Nativo en Java:** Construida íntegramente en Java 11 con Android Studio y AndroidX, utilizando componentes de Material Design.
2. **Funcionamiento 100% Offline con SQLite:** La aplicación no depende de conexiones a internet ni de APIs externas para funcionar. Toda la lógica de negocio y persistencia se ejecuta en una base de datos SQLite local (`casholocal.db`).
3. **Autenticación Biométrica:** Implementada mediante la API estándar `androidx.biometric.BiometricPrompt` en el inicio de la aplicación, solicitando huella dactilar o credencial del dispositivo para autorizar el acceso.
4. **Interacción con Mapas:** Implementación de mapas espaciales mediante **OSMdroid** (OpenStreetMap), permitiendo ubicar, explorar y filtrar movimientos financieros por ubicación geográfica de manera fluida y sin requerir servicios privativos de Google Play.

---

## 3. Arquitectura de la Aplicación

La aplicación sigue una arquitectura modular desacoplada basada en el patrón Modelo-Vista-Controlador (MVC) adaptado a Android:

* **Controladores y Vistas:**
  * `MainActivity`: Actividad principal que administra el ciclo de vida, la autenticación biométrica con `BiometricPrompt` y el menú de navegación inferior.
  * `MapActivity`: Actividad dedicada al mapa interactivo OSMdroid con filtrado dinámico de marcadores y tarjeta de detalle.
  * `HomeFragment`: Panel principal con balance general, resumen de deudas, cuentas y transacciones recientes.
  * `PurchasesFragment`: Gestión de compras con pestañas de "Realizadas" y "Agendadas".
  * `SharedFragment`: Módulo de gastos grupales y seguimiento de saldos ("Me deben" / "Debo pagar").
  * `ContactsFragment`: Agenda local de contactos para vinculación de gastos.
  * Diálogos modales: `AddTransactionDialogFragment`, `AddSharedExpenseDialogFragment`, `AddContactDialogFragment`.
* **Modelos (POJO):**
  * `CuentaModel`: Datos de cuentas bancarias, efectivo y ahorros.
  * `MovimientoModel`: Registro de ingresos y egresos con categoría y coordenadas GPS.
  * `ContactoModel`: Contactos de la agenda interna.
  * `CompraModel`: Compras realizadas o programadas a futuro.
  * `DeudaModel`: Registro de deudas individuales derivadas de compras grupales.
* **Persistencia:**
  * `DatabaseHelper`: Subclase de `SQLiteOpenHelper` que centraliza la creación de tablas, llaves foráneas y operaciones CRUD transaccionales.

---

## 4. Estructura de la Base de Datos SQLite

Base de datos: `casholocal.db` (Versión 5).

### 4.1 Tablas y Atributos

```sql
-- 1. Tabla Cuentas
CREATE TABLE cuentas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT,
    tipo TEXT,
    saldo_inicial REAL,
    moneda TEXT,
    descripcion TEXT,
    fecha_creacion TEXT
);

-- 2. Tabla Movimientos
CREATE TABLE movimientos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cuenta_id INTEGER,
    tipo TEXT, -- 'ingreso' o 'egreso'
    monto REAL,
    descripcion TEXT,
    fecha TEXT,
    latitud REAL,
    longitud REAL,
    categoria TEXT,
    FOREIGN KEY(cuenta_id) REFERENCES cuentas(id) ON DELETE CASCADE
);

-- 3. Tabla Contactos
CREATE TABLE contactos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT,
    telefono TEXT,
    email TEXT,
    notas TEXT
);

-- 4. Tabla Compras
CREATE TABLE compras (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo TEXT,
    monto REAL,
    fecha TEXT,
    es_realizada INTEGER, -- 1 = Realizada, 0 = Agendada
    contacto_id INTEGER,
    categoria TEXT,
    FOREIGN KEY(contacto_id) REFERENCES contactos(id) ON DELETE SET NULL
);

-- 5. Tabla Gastos Compartidos
CREATE TABLE gastos_compartidos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    concepto TEXT,
    monto_total REAL,
    tipo_division TEXT, -- 'equitativa' o 'personalizada'
    pagador TEXT,
    fecha TEXT
);

-- 6. Tabla Gasto Items
CREATE TABLE gasto_items (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    gasto_id INTEGER,
    nombre_item TEXT,
    precio_item REAL,
    FOREIGN KEY(gasto_id) REFERENCES gastos_compartidos(id) ON DELETE CASCADE
);

-- 7. Tabla Deudas
CREATE TABLE deudas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    gasto_id INTEGER,
    contacto_nombre TEXT,
    monto_deuda REAL,
    es_pagada INTEGER, -- 1 = Pagada, 0 = Pendiente
    tipo_deuda TEXT, -- 'me_deben' o 'yo_debo'
    FOREIGN KEY(gasto_id) REFERENCES gastos_compartidos(id) ON DELETE CASCADE
);
```

---

## 5. Dependencias del Proyecto

Definidas en `app/build.gradle`:

* `androidx.appcompat:appcompat:1.8.0`
* `com.google.android.material:material:1.14.0`
* `androidx.constraintlayout:constraintlayout:2.2.2`
* `androidx.cardview:cardview:1.0.0`
* `androidx.recyclerview:recyclerview:1.4.0`
* `androidx.biometric:biometric:1.1.0` (Autenticación Biométrica oficial)
* `org.osmdroid:osmdroid-android:6.1.18` (Mapas espaciales OpenStreetMap)

---

## 6. Guía de Compilación y Ejecución

1. **Requisitos Previos:**
   * Android Studio Ladybug / Meerkat o superior.
   * JDK 11 configurado en Android Studio.
   * SDK de Android con soporte para `minSdk 26` y `compileSdk 36`.
2. **Pasos para Ejecutar:**
   * Clonar el repositorio:
     ```bash
     git clone <url-del-repositorio>
     ```
   * Abrir el proyecto en Android Studio (`File > Open...`).
   * Esperar a que Gradle sincronice las dependencias automáticamente.
   * Seleccionar un emulador con API 26 o superior (o dispositivo físico).
   * Presionar el botón **Run ('app')** (Shift + F10).
3. **Prueba de Biometría:**
   * En dispositivo físico: Usar el sensor de huella dactilar.
   * En emulador de Android Studio: Ir a los tres puntos del emulador (`...` > Fingerprint) y simular el toque de huella. Si el emulador no tiene huella configurada, el sistema permite el ingreso tras notificarlo para asegurar la evaluación.

---
