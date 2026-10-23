# ContactSphere — MAD Experiment 10

**Data Sharing Using Contacts Content Provider**

ContactSphere is a modern Android application designed and implemented for MAD Experiment 10. It reads device contact records securely using Android's `ContentResolver` and `ContactsContract` API, displaying them in a premium purple-themed, real-time searchable interface.

---

## 🌟 Key Features

1. **Premium Purple Theme & UI**:
   - Custom purple gradient header with live contact counter pill.
   - Rounded Material 3 contact cards with dynamically colored circular avatars featuring contact initials.

2. **Contacts Content Provider Integration**:
   - Uses `ContentResolver.query()` on `ContactsContract.CommonDataKinds.Phone.CONTENT_URI` to read contact names and phone numbers.
   - Handles deduplication and sorting by display name.

3. **Real-time Search & Filter**:
   - Instant filtering as the user types name or phone number.
   - Empty state illustration and helpful message when no matches are found.

4. **Runtime Permission Lifecycle**:
   - Requests `READ_CONTACTS` at runtime using `ActivityResultContracts.RequestPermission()`.
   - Clean permission-denied fallback state with a direct "Grant Permission" trigger button.

---

## 📁 File Structure

- `AndroidManifest.xml`: Declares `READ_CONTACTS` permission.
- `MainActivity.kt`: Handles runtime permission requests, content provider queries, and UI updates.
- `ContactAdapter.kt`: RecyclerView adapter for contacts with search filter and initial avatars.
- `ContactItem.kt`: Data model for contact entry.
- `activity_main.xml`: Purple header, contact counter badge, search card, contacts list, and empty/error states.
- `item_contact.xml`: Material Card item layout for individual contact records.
- `colors.xml` & `themes.xml`: Premium purple palette definitions.

---

## 🧪 Test Cases

| Test Case | Scenario | Expected Result |
| :--- | :--- | :--- |
| **TC01** | First launch | App prompts for `READ_CONTACTS` permission |
| **TC02** | Permission Granted | Device contacts load into the list and total count updates |
| **TC03** | Search Name | List filters dynamically to show contacts matching name |
| **TC04** | Search Phone | List filters dynamically to show contacts matching phone number |
| **TC05** | No Match | "No Contacts Found" empty state view is displayed |
| **TC06** | Permission Denied | "Permission Required" state view with "Grant Permission" button is displayed |
